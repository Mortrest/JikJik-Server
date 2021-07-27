package server.shared;

import server.model.Models.Tweet;

import java.util.LinkedList;

public class GetTweetsResponse {

    LinkedList<Tweet> tweets;

    public GetTweetsResponse(LinkedList<Tweet> tweets) {
        this.tweets = tweets;
    }

    public LinkedList<Tweet> getTweets() {
        return tweets;
    }
}
