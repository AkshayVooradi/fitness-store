import { Fragment, useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Button } from "@/components/ui/button";
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet";
import CommonForm from "@/components/common/form";
import ProductImageUpload from "@/components/admin-view/image-upload";
import AdminProductTile from "@/components/admin-view/product-tile";
import { useToast } from "@/components/ui/use-toast";
import { addProductFormElements } from "@/config";
import {
  addNewProduct,
  deleteProduct,
  editProduct,
  fetchAllProducts,
} from "@/store/admin/products-slice";

const initialFormData = {
  title: "",
  description: "",
  category: "Men",
  brand: "",
  price: "",
  salePrice: "",
  totalStock: "",
  averageReview: 0,
  image: "",
};

function AdminProducts() {
  const [openCreateProductsDialog, setOpenCreateProductsDialog] = useState(false);
  const [formData, setFormData] = useState(initialFormData);
  const [imageFile, setImageFile] = useState(null);
  const [imageLoadingState, setImageLoadingState] = useState(false);

  const editProductIdRef = useRef(null);
  const isEditModeRef = useRef(false);

  const { productList } = useSelector((state) => state.adminProducts);
  const dispatch = useDispatch();
  const { toast } = useToast();
  const token = localStorage.getItem("token");

  const resetForm = () => {
    setFormData(initialFormData);
    setImageFile(null);
    isEditModeRef.current = false;
    editProductIdRef.current = null;
    setOpenCreateProductsDialog(false);
  };

  useEffect(() => {
    if (token) {
      dispatch(fetchAllProducts({ token }));
    }
  }, [dispatch, token]);

  const isFormValid = () => {
    const requiredFieldsFilled = Object.keys(formData)
      .filter((key) => key !== "averageReview" && key !== "image")
      .every((key) => formData[key] !== "");

    return isEditModeRef.current ? requiredFieldsFilled : requiredFieldsFilled && imageFile;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const isEdit = isEditModeRef.current;
    const editedId = editProductIdRef.current;

    if (isEdit && !editedId) {
      toast({
        title: "⚠️ Cannot edit — Product ID is missing!",
        variant: "destructive",
      });
      return;
    }

    if (isEdit) {
      dispatch(
        editProduct({
          id: editedId,
          formData: {
            title: formData.title,
            description: formData.description,
            category: formData.category,
            brand: formData.brand,
            price: formData.price,
            salePrice: formData.salePrice,
            stock: formData.totalStock,
          },
          token,
        })
      ).then((res) => {
        if (res?.payload) {
          toast({ title: "✅ Product updated!" });
          resetForm();
          dispatch(fetchAllProducts({ token }));
        }
      });
    } else {
      const data = new FormData();
      data.append("images", imageFile);
      data.append("title", formData.title);
      data.append("description", formData.description);
      data.append("category", formData.category);
      data.append("brand", formData.brand);
      data.append("price", formData.price);
      data.append("salePrice", formData.salePrice);
      data.append("stock", formData.totalStock);

      dispatch(addNewProduct({ formData: data, token })).then((res) => {
        if (res?.payload) {
          toast({ title: "✅ Product added!" });
          resetForm();
          dispatch(fetchAllProducts({ token }));
        }
      });
    }
  };

  const handleDelete = (title) => {
    dispatch(deleteProduct(title)).then((res) => {
      if (res?.payload) {
        toast({ title: "🗑️ Product deleted" });
        dispatch(fetchAllProducts({ token }));
      }
    });
  };

  return (
    <Fragment>
      <div className="mb-5 w-full flex justify-end">
        <Button
          onClick={() => {
            resetForm();
            setOpenCreateProductsDialog(true);
          }}
        >
          Add New Product
        </Button>
      </div>

      <div className="grid gap-4 md:grid-cols-3 lg:grid-cols-4">
        {productList?.length > 0 &&
          productList.map((product) => (
            <AdminProductTile
              key={product._id}
              product={product}
              setFormData={setFormData}
              setOpenCreateProductsDialog={setOpenCreateProductsDialog}
              handleDelete={handleDelete}
              editProductIdRef={editProductIdRef}
              isEditModeRef={isEditModeRef}
            />
          ))}
      </div>

      <Sheet
        open={openCreateProductsDialog}
        onOpenChange={(isOpen) => {
          if (!isOpen) resetForm();
        }}
      >
        <SheetContent side="right" className="overflow-auto">
          <SheetHeader>
            <SheetTitle>
              {isEditModeRef.current
                ? `Edit Product (ID: ${editProductIdRef.current})`
                : "Add New Product"}
            </SheetTitle>
          </SheetHeader>

          <ProductImageUpload
            imageFile={imageFile}
            setImageFile={setImageFile}
            imageLoadingState={imageLoadingState}
            setImageLoadingState={setImageLoadingState}
            isEditMode={isEditModeRef.current}
            uploadedImageUrl={formData.image}
          />

          <div className="py-6">
            <CommonForm
              onSubmit={handleSubmit}
              formData={formData}
              setFormData={setFormData}
              buttonText={isEditModeRef.current ? "Edit" : "Add"}
              formControls={addProductFormElements}
              isBtnDisabled={!isFormValid()}
            />
          </div>
        </SheetContent>
      </Sheet>
    </Fragment>
  );
}

export default AdminProducts;
