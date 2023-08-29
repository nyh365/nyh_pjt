package com.pjt.nyh.repository;

import com.pjt.nyh.config.JpaConfig;
import com.pjt.nyh.domain.Article;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("조회 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine(){
        List<Article> articleList = articleRepository.findAll();

        org.assertj.core.api.Assertions.assertThat(articleList)
                .isNotNull().hasSize(100);
    }

    @DisplayName("업데이트 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine(){
        Article findArticle = articleRepository.findById(1L).orElse(null);

        findArticle.setHashtag("Spring");

        Article article = articleRepository.save(findArticle);

        org.assertj.core.api.Assertions.assertThat(article).hasFieldOrPropertyWithValue("hashtag", "Spring");
    }

    @DisplayName("삭제 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElse(null);
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        // When
        articleRepository.delete(article);

        // Then
        org.assertj.core.api.Assertions.assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        org.assertj.core.api.Assertions.assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }
}