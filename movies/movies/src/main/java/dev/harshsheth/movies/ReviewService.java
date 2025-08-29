package dev.harshsheth.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId)
    {
        Review review = reviewRepository.insert(new Review(reviewBody));
        System.out.println("Pushing review data for imdb Id : " + imdbId);
        // Mongo template is used to run complex queries, it gives fine grained control over mongo database to perform various operations
        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();

        System.out.println("Pused review data for imdb Id : " + imdbId);

        return review;
    }

}
