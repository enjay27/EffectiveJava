package com.enjay27;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EffectiveJava {

    public static void main(String[] args) {

    }

    public Article getArticleByIdV2() {
        ArticleDetail articleDetail = new ArticleDetail();

        Article article = Optional.ofNullable(articleDetail)
                .stream()
                .peek(ArticleDetail::validAndValuateArticle)
                .map(ArticleDetail::getArticle)
                .findAny().get();

        return article;
    }

    public Article getArticleById(int id) {
        ArticleDetail articleDetail = new ArticleDetail();

        Article article = null;

        if (articleDetail != null) {
            article = articleDetail.getArticle();
            List<String> thumbnailFile = articleDetail.getThumbnailFile();

            if (article != null && thumbnailFile != null) {
                article.setThumbnailFile(thumbnailFile);
            }
        }

        return article;
    }

    static class ArticleDetail {

        private Article article;
        private List<String> thumbnailFile;

        public Article getArticle() {
            return article;
        }

        public List<String> getThumbnailFile() {
            return thumbnailFile;
        }

        public static void validAndValuateArticle(ArticleDetail articleDetail) {
            if (articleDetail.hasArticle() && articleDetail.hasThumbnailUrl())
                articleDetail.getArticle().setThumbnailFile(articleDetail.getThumbnailFile());
        }

        public boolean hasArticle() {
            return article != null;
        }

        public boolean hasThumbnailUrl() {
            return thumbnailFile != null;
        }
    }

    static class Article {
        List<String> thumbnailFile;

        public List<String> getThumbnailFile() {
            return thumbnailFile;
        }

        public void setThumbnailFile(List<String> thumbnailFile) {
            this.thumbnailFile = thumbnailFile;
        }
    }




}

