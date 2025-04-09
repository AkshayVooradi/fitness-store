import { Label } from "../ui/label";
import StarRatingComponent from "../common/star-rating";
import { Input } from "../ui/input";
import { useDispatch } from "react-redux";
import { addReview, getReviews } from "@/store/shop/review-slice";
import { useState } from "react";
import { useToast } from "../ui/use-toast";
import { Button } from "../ui/button";

function WriteReview(id) {
  const [reviewMsg, setReviewMsg] = useState("");
  const [rating, setRating] = useState(0);
  const { toast } = useToast();
  const dispatch = useDispatch();

  function handleRatingChange(getRating) {
    setRating(getRating);
  }

  function handleAddReview() {
    dispatch(
      addReview({
        productId: id.productId,
        reviewMessage: reviewMsg,
        reviewValue: rating,
      })
    ).then((data) => {
      if (data?.payload?.success) {
        setRating(0);
        setReviewMsg("");
        dispatch(getReviews(id.productId));
        toast({
          title: data?.payload?.message,
        });
      } else {
        toast({
          title: data.payload.message,
          variant: "destructive",
        });
      }
    });
  }

  return (
    <div className="mt-10 flex-col flex gap-2">
      <Label>Write a review</Label>
      <div className="flex gap-1">
        <StarRatingComponent
          rating={rating}
          handleRatingChange={handleRatingChange}
        />
      </div>
      <Input
        name="reviewMsg"
        value={reviewMsg}
        onChange={(event) => setReviewMsg(event.target.value)}
        placeholder="Write a review..."
      />
      <Button onClick={handleAddReview} disabled={reviewMsg.trim() === ""}>
        Submit
      </Button>
    </div>
  );
}

export default WriteReview;
