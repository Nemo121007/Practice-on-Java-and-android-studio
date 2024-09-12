package GameSettings;

public class GameSettings {
    private static int maxScore;
    private static int enemyScore;
    private static int userScore;

    public static int getMaxScore() {
        return maxScore;
    }

    public static void setMaxScore(int maxScore) {
        if (maxScore > 0)
            GameSettings.maxScore = maxScore;
    }

    public static int getEnemyScore() {
        return enemyScore;
    }

    public static void setEnemyScore(int enemyScore) {
        if (enemyScore >= 0)
            GameSettings.enemyScore = enemyScore;
    }

    public static int getUserScore() {
        return userScore;
    }

    public static void setUserScore(int userScore) {
        if (userScore >= 0)
            GameSettings.userScore = userScore;
    }
}