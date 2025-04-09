import { useNavigate } from "react-router-dom";
import { Button } from "../ui/button";
import { SheetContent, SheetHeader, SheetTitle } from "../ui/sheet";
import UserCartItemsContent from "./cart-items-content";
import { useDispatch } from "react-redux";
import { createOrder } from "@/store/shop/order-slice";
import { data } from "autoprefixer";
import { toast } from "../ui/use-toast";
import { fetchCartItems } from "@/store/shop/cart-slice";
import { useEffect, useState } from "react";

function UserCartWrapper({ cartItems, setOpenCartSheet }) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [cartInfo, setCartInfo] = useState(null);

  useEffect(() => {
    if (cartItems) {
      setCartInfo(cartItems);
    }
  }, [cartItems]);

  const totalCartAmount =
    cartInfo && cartInfo.length > 0
      ? cartInfo.reduce(
          (sum, currentItem) =>
            sum +
            (currentItem?.salePrice > 0
              ? currentItem?.salePrice
              : currentItem?.price) *
              currentItem?.quantity,
          0
        )
      : 0;

  const handleCheckout = () => {
    const formData = {
      totalCartAmount,
    };

    dispatch(createOrder(formData)).then((data) => {
      if (data?.payload?.success) {
        toast({
          title: data.payload.message,
        });
        dispatch(fetchCartItems());
      } else {
        toast({
          title: data.payload.message,
          vatiant: "destructive",
        });
      }
    });
  };

  return (
    <SheetContent className="sm:max-w-md">
      <SheetHeader>
        <SheetTitle>Your Cart</SheetTitle>
      </SheetHeader>
      <div className="mt-8 space-y-4">
        {cartInfo && cartInfo.length > 0
          ? cartInfo.map((item) => <UserCartItemsContent cartItem={item} />)
          : null}
      </div>
      <div className="mt-8 space-y-4">
        <div className="flex justify-between">
          <span className="font-bold">Total</span>
          <span className="font-bold">${totalCartAmount}</span>
        </div>
      </div>
      <Button onClick={handleCheckout} className="w-full mt-6">
        Checkout
      </Button>
    </SheetContent>
  );
}

export default UserCartWrapper;
