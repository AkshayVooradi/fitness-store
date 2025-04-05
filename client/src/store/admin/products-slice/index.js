import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

const initialState = {
  isLoading: false,
  productList: [],
};

export const addNewProduct = createAsyncThunk(
  "/products/addnewproduct",
  async ({ formData, token }) => {
    const response = await axios.post(
      "http://localhost:8080/api/admin/product/add",
      formData,
      {
        headers: {
          Authorization: token ? `Bearer ${token}` : "",
          "Content-Type": "multipart/form-data",
        },
      }
    );

    return response?.data;
  }
);

export const fetchAllProducts = createAsyncThunk(
  "/products/fetchAllProducts",
  async ({ token }) => {
    const response = await axios.get(
      "http://localhost:8080/api/admin/product/getAllProducts",
      {
        headers: {
          Authorization: token ? `Bearer ${token}` : "",
        },
      }
    );

    return response?.data;
  }
);

export const editProduct = createAsyncThunk(
  "/products/editProduct",
  async ({ id, formData, token }) => {
    const result = await axios.put(
      `http://localhost:8080/api/admin/product/edit/${id}`, 
      formData,
      {
        headers: {
          Authorization: token ? `Bearer ${token}` : "",
          "Content-Type": "application/json",
        },
      }
    );
    return result?.data;
  }
);

export const deleteProduct = createAsyncThunk(
  "/products/deleteProduct",
  async (title) => {
    const token = localStorage.getItem("token");

    const response = await axios.delete(
      `http://localhost:8080/api/admin/product/delete?title=${title}`,
      {
        headers: {
          Authorization: token ? `Bearer ${token}` : "",
        },
      }
    );

    return response?.data;
  }
);

const AdminProductsSlice = createSlice({
  name: "adminProducts",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchAllProducts.pending, (state) => {
        state.isLoading = true;
      })
      .addCase(fetchAllProducts.fulfilled, (state, action) => {
        state.isLoading = false;
        state.productList = action.payload;
      })
      .addCase(fetchAllProducts.rejected, (state) => {
        state.isLoading = false;
        state.productList = [];
      })
      .addCase(deleteProduct.fulfilled, (state, action) => {
        const deletedTitle = action.payload?.title;
        if (deletedTitle) {
          state.productList = state.productList.filter(
            (product) => product.title !== deletedTitle
          );
        }
      });
  },
});

export default AdminProductsSlice.reducer;
