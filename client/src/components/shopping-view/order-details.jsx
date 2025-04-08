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
import { useState } from "react";
import WriteReview from "./review";

function ShoppingOrderDetailsView({ orderDetails }) {
  const dispatch = useDispatch();
  const [openReviewDialog, setOpenReviewDialog] = useState(false);
  const [selectedProductId, setSelectedProductId] = useState(null);

  const handleOpenReview = (productId) => {
    setSelectedProductId(productId);
    setOpenReviewDialog(true);
  };

  return (
    <DialogContent className="sm:max-w-[600px]">
      <div className="grid gap-6">
        <div className="grid gap-2">
          <div className="flex mt-6 items-center justify-between">
            <p className="font-medium">Order ID</p>
            <Label>{orderDetails?.id}</Label>
          </div>
          <div className="flex mt-2 items-center justify-between">
            <p className="font-medium">Order Date</p>
            <Label>{orderDetails?.orderDate.split("T")[0]}</Label>
          </div>
          <div className="flex mt-2 items-center justify-between">
            <p className="font-medium">Order Price</p>
            <Label>${orderDetails?.totalAmount}</Label>
          </div>
          <div className="flex mt-2 items-center justify-between">
            <p className="font-medium">Order Status</p>
            <Label>
              <Badge
                className={`py-1 px-3 ${
                  orderDetails?.orderStatus === "confirmed"
                    ? "bg-green-500"
                    : orderDetails?.orderStatus === "rejected"
                    ? "bg-red-600"
                    : "bg-black"
                }`}
              >
                {orderDetails?.orderStatus}
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
                  {orderDetails?.orderStatus === "delivered" ? (
                    <TableHead>Reviews</TableHead>
                  ) : null}
                </TableRow>
              </TableHeader>
              <TableBody>
                {orderDetails?.cartItems && orderDetails?.cartItems.length > 0
                  ? orderDetails?.cartItems.map((item) => (
                      <TableRow>
                        <TableCell>{item.title}</TableCell>
                        <TableCell>{item.quantity}</TableCell>
                        <TableCell>{item.price}</TableCell>
                        {orderDetails?.orderStatus === "delivered" ? (
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
            {/* <ul className="grid gap-3">
              {orderDetails?.cartItems && orderDetails?.cartItems.length > 0
                ? orderDetails?.cartItems.map((item) => (
                    <li className="flex items-center justify-between">
                      <span>Title: {item.title}</span>
                      <span>Quantity: {item.quantity}</span>
                      <span>Price: ${item.price}</span>
                      {orderDetails?.orderStatus === "delivered" ? (
                        // <div className="mt-10 flex-col flex gap-2">
                        //   <Label>Write a review</Label>
                        //   <div className="flex gap-1">
                        //     <StarRatingComponent
                        //       rating={rating}
                        //       handleRatingChange={handleRatingChange}
                        //     />
                        //   </div>
                        //   <Input
                        //     name="reviewMsg"
                        //     value={reviewMsg}
                        //     onChange={(event) =>
                        //       setReviewMsg(event.target.value)
                        //     }
                        //     placeholder="Write a review..."
                        //   />
                        //   <Button
                        //     onClick={handleAddReview}
                        //     disabled={reviewMsg.trim() === ""}
                        //   >
                        //     Submit
                        //   </Button>
                        // </div>
                        <span>
                          <Button className="w-full" onClick={() => {}}>
                            Write Review
                          </Button>
                        </span>
                      ) : null}
                    </li>
                  ))
                : null}
            </ul> */}
          </div>
        </div>
        <div className="grid gap-4">
          {orderDetails?.orderStatus !== "delivered" &&
          orderDetails?.orderStatus !== "rejected" ? (
            <div className="grid gap-2">
              <div className="font-medium">Cancel Order</div>
              <div className="grid gap-0.5 text-muted-foreground">
                <Button
                  className="w-full"
                  onClick={() => {
                    handleCancelOrder(orderDetails?.id);
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
