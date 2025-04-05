import { Button } from "../ui/button";
import { Card, CardContent, CardFooter } from "../ui/card";

function AdminProductTile({
  product,
  setFormData,
  setOpenCreateProductsDialog,
  editProductIdRef,
  isEditModeRef,
  handleDelete,
}) {
  const handleEdit = () => {
    let rawId = product?._id || product?.id;
    let id = "";

    if (typeof rawId === "string") {
      id = rawId;
    } else if (typeof rawId === "object" && rawId !== null) {
      if (rawId.$oid) {
        id = rawId.$oid;
      } else if (rawId.toHexString) {
        id = rawId.toHexString();
      } else if (rawId.timestamp || rawId.date) {
        return;
      } else {
        id = String(rawId);
      }
    } else {
      return;
    }

    if (!id || id === "undefined" || id.includes("[object")) {
      return;
    }

    editProductIdRef.current = id;
    isEditModeRef.current = true;

    Promise.resolve().then(() => {
      setFormData({
        title: product.title,
        description: product.description,
        category: product.category,
        brand: product.brand,
        price: product.price,
        salePrice: product.salePrice,
        totalStock: product.stock,
        averageReview: product.averageRating || 0,
        image: product.imageUrl?.[0] || "",
      });

      Promise.resolve().then(() => {
        setOpenCreateProductsDialog(true);
      });
    });
  };

  return (
    <Card className="w-full max-w-sm mx-auto">
      <div>
        <div className="relative">
          <img
            src={product?.imageUrl?.[0] || "/no-image.png"}
            alt={product?.title}
            className="w-full h-[300px] object-cover rounded-t-lg bg-gray-100"
          />
        </div>
        <CardContent>
          <h2 className="text-xl font-bold mb-2 mt-2 capitalize">{product?.title}</h2>
          <div className="flex justify-between items-center mb-2">
            <span
              className={`${
                product?.salePrice > 0 ? "line-through" : ""
              } text-lg font-semibold text-primary`}
            >
              ₹{product?.price}
            </span>
            {product?.salePrice > 0 && (
              <span className="text-lg font-bold text-green-600">₹{product?.salePrice}</span>
            )}
          </div>
        </CardContent>
        <CardFooter className="flex justify-between items-center">
          <Button onClick={handleEdit}>Edit</Button>
          <Button
            onClick={() => {
              if (product?.title) {
                handleDelete(product.title);
              }
            }}
            variant="destructive"
          >
            Delete
          </Button>
        </CardFooter>
      </div>
    </Card>
  );
}

export default AdminProductTile;
