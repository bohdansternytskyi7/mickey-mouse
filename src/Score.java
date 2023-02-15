import java.io.Serializable;

public class Score implements Serializable {
    private int score;
    private String nickname;

    public Score(int score, String nickname) {
        this.score = score;
        this.nickname = nickname;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return this.nickname + ":\t " + this.score;
    }
}
