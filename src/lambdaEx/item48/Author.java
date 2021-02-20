package lambdaEx.item48;

public class Author {
    private String name;
    private int relatedArticleId;

    public Author(String name, int relatedArticleId) {
        this.name = name;
        this.relatedArticleId = relatedArticleId;
    }

    public String getName() {
        return name;
    }

    public int getRelatedArticleId() {
        return 0;
    }

    @Override
    public String toString() {
        return relatedArticleId + name;
    }
}
