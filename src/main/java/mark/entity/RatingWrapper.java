package mark.entity;

public class RatingWrapper {
    private int firstRating;
    private int secondRating;

    public RatingWrapper() {
    }

    public void setFirstRating(int firstRating) {
        this.firstRating = firstRating;
    }

    public void setSecondRating(int secondRating) {
        this.secondRating = secondRating;
    }

    public int getFirstRating() {
        return firstRating;
    }

    public int getSecondRating() {
        return secondRating;
    }
}
