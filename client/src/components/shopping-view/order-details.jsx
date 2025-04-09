import { Badge } from "../ui/badge";
import { DialogContent, Dialog } from "../ui/dialog";
import { Label } from "../ui/label";
import { Separator } from "../ui/separator";
import { Button } from "../ui/button";
import { useDispatch } from "react-redux";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../ui/table";
import { useEffect, useState } from "react";
import WriteReview from "./review";

import {
  cancelOrderById,
  getAllOrdersByUserId,
  getOrderDetails,
} from "@/store/shop/order-slice";
import { useToast } from "../ui/use-toast";

function ShoppingOrderDetailsView({ orderDetails }) {
  const dispatch = useDispatch();
  const [openReviewDialog, setOpenReviewDialog] = useState(false);
  const [selectedProductId, setSelectedProductId] = useState(null);
  const { toast } = useToast();
  const [orderInfo, setOrderInfo] = useState(null);

  useEffect(() => {
    if (orderDetails) {
      setOrderInfo(orderDetails);
    }
  }, [orderDetails]);

  const handleOpenReview = (productId) => {
    setSelectedProductId(productId);
    setOpenReviewDialog(true);
  };

  const handleCancelOrder = (orderId) => {
    dispatch(cancelOrderById(orderId)).then((data) => {
      if (data?.payload?.success) {
        toast({
          title: data?.payload?.message,
        });
        setOrderInfo((prev) => ({
          ...prev,
          orderStatus: "cancelled",
        }));
        dispatch(getAllOrdersByUserId());
      } else {
        toast({
          title: data?.payload?.message,
          variant: "destructive",
        });
      }
    });
  };

  return (
    <DialogContent className="sm:max-w-[600px]">
      <div className="grid gap-6">
        <div className="grid gap-2">
          <div className="flex mt-6 items-center justify-between">
            <p className="font-medium">Order ID</p>
            <Label>{orderInfo?.id}</Label>
          </div>
          <div className="flex mt-2 items-center justify-between">
            <p className="font-medium">Order Date</p>
            <Label>{orderInfo?.orderDate.split("T")[0]}</Label>
          </div>
          <div className="flex mt-2 items-center justify-between">
            <p className="font-medium">Order Price</p>
            <Label>${orderInfo?.totalAmount}</Label>
          </div>
          <div className="flex mt-2 items-center justify-between">
            <p className="font-medium">Order Status</p>
            <Label>
              <Badge
                className={`py-1 px-3 ${
                  orderInfo?.orderStatus === "confirmed"
                    ? "bg-green-500"
                    : orderInfo?.orderStatus === "rejected" ||
                      orderInfo?.orderStatus === "cancelled"
                    ? "bg-red-600"
                    : "bg-black"
                }`}
              >
                {orderInfo?.orderStatus}
              </Badge>
            </Label>
          </div>
        </div>
        <Separator />
        <div className="grid gap-4">
          <div className="grid gap-2">
            <div className="font-medium">Order Details</div>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Title</TableHead>
                  <TableHead>Quantity</TableHead>
                  <TableHead>Price</TableHead>
                  {orderInfo?.orderStatus === "delivered" ? (
                    <TableHead>Reviews</TableHead>
                  ) : null}
                </TableRow>
              </TableHeader>
              <TableBody>
                {orderInfo?.cartItems && orderInfo?.cartItems.length > 0
                  ? orderInfo?.cartItems.map((item) => (
                      <TableRow>
                        <TableCell>{item.title}</TableCell>
                        <TableCell>{item.quantity}</TableCell>
                        <TableCell>{item.price}</TableCell>
                        {orderInfo?.orderStatus === "delivered" ? (
                          <TableCell>
                            <Button
                              className="w-full"
                              onClick={() => {
                                handleOpenReview(item.productId);
                              }}
                            >
                              Write Review
                            </Button>
                          </TableCell>
                        ) : null}
                      </TableRow>
                    ))
                  : null}
              </TableBody>
              <Dialog
                open={openReviewDialog}
                onOpenChange={setOpenReviewDialog}
              >
                <DialogContent>
                  <WriteReview productId={selectedProductId} />
                </DialogContent>
              </Dialog>
            </Table>
          </div>
        </div>
        <div className="grid gap-4">
          {orderInfo?.orderStatus !== "delivered" &&
          orderInfo?.orderStatus !== "rejected" &&
          orderInfo?.orderStatus !== "cancelled" ? (
            <div className="grid gap-2">
              <div className="font-medium">Cancel Order</div>
              <div className="grid gap-0.5 text-muted-foreground">
                <Button
                  className="w-full"
                  onClick={() => {
                    handleCancelOrder(orderInfo?.id);
                  }}
                >
                  Cancel Order
                </Button>
              </div>
            </div>
          ) : null}
        </div>
      </div>
    </DialogContent>
  );
}

export default ShoppingOrderDetailsView;
