class Tweet implements Comparable<Tweet> {
    int time;
    int id;

    Tweet(int time, int id) {
        this.time = time;
        this.id = id;
    }

    public int compareTo(Tweet that) {
        return that.time - this.time; 
    }
}

class User {
    int userid;
    HashSet<Integer> followers;
    List<Tweet> tweets;

    User(int id) {
        this.userid = id;
        this.followers = new HashSet<>();
        this.tweets = new LinkedList<>();
    }

    void addTweet(Tweet t) {
        tweets.add(0, t); 
    }

    void addFollower(int userid) {
        followers.add(userid);
    }

    void removeFollower(int userid) {
        followers.remove(userid);
    }
}

class Twitter {
    HashMap<Integer, User> userMap;
    int time;

    public Twitter() {
        userMap = new HashMap<>();
        time = 0;
    }

    public void postTweet(int userId, int tweetId) {
        time++;
        userMap.putIfAbsent(userId, new User(userId));
        User user = userMap.get(userId);
        user.addTweet(new Tweet(time, tweetId));
    }

    public List<Integer> getNewsFeed(int userId) {
        if (!userMap.containsKey(userId)) return new ArrayList<>();

        PriorityQueue<Tweet> pq = new PriorityQueue<>();
        User user = userMap.get(userId);

        for (Tweet tweet : user.tweets) {
            pq.offer(tweet);
        }

        for (int followerId : user.followers) {
            if (!userMap.containsKey(followerId)) continue;
            int count = 0;
            for (Tweet tweet : userMap.get(followerId).tweets) {
                pq.offer(tweet);
                count++;
                if (count >= 10) break;
            }
        }

        List<Integer> result = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < 10) {
            result.add(pq.poll().id);
            count++;
        }

        return result;
    }

    public void follow(int followerId, int followeeId) {
        userMap.putIfAbsent(followerId, new User(followerId));
        userMap.putIfAbsent(followeeId, new User(followeeId));
        userMap.get(followerId).addFollower(followeeId);
    }

    public void unfollow(int followerId, int followeeId) {
        if (!userMap.containsKey(followerId) || followerId == followeeId) return;
        userMap.get(followerId).removeFollower(followeeId);
    }
}
