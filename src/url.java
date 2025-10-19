
class Url {
    String url;
    int expiryDate;
    int startDate;

    Url(String url, int expiryDate, int startDate) {
        this.url = url;
        this.expiryDate = expiryDate;
        this.startDate = startDate;
    }
}

class UrlShortner {
    Map<String, String> longToShort = new HashMap<>();
    int counter;

    UrlShortner(Map<String, String> longToShort) {
        this.longToShort = longToShort;
        this.counter = 0;
    }
}

class Shortner {
    UrlShortner url;
    final String BASE_URL = "http://short.url/";
    final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String recommend;

    Shortner(UrlShortner url, String recommend) {
        this.url = url;
        this.recommend = recommend;
    }

    String longToShort(Url longUrlObj, int currentDate) {
        // Check start and expiry date
        if (currentDate < longUrlObj.startDate) {
            return "Error: URL cannot be shortened yet. Start date not reached.";
        }
        if (currentDate > longUrlObj.expiryDate) {
            return "Error: URL expired. Cannot shorten.";
        }

        String longUrl = longUrlObj.url;

        if (url.longToShort.containsKey(longUrl)) {
            return url.longToShort.get(longUrl);
        } else {
            String shortStr = generateShortUrl(url.counter++);
            String shortUrl = BASE_URL + shortStr + recommend;
            url.longToShort.put(longUrl, shortUrl);
            return shortUrl;
        }
    }

    private String generateShortUrl(int num) {
        if (num == 0) return "0";

        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(BASE62.charAt(num % 62));
            num /= 62;
        }
        return sb.reverse().toString();
    }
}

class Main {
    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        UrlShortner url = new UrlShortner(map);
        Shortner shortner = new Shortner(url, "ayush");

        int currentDate = 20251019;

        Url longUrl1 = new Url("https://example.com/ayush", 20251030, 20251018);
        Url longUrl2 = new Url("https://example.com/test", 20251018, 20251020); 
        Url longUrl3 = new Url("https://example.com/expired", 20251018, 20251010);

        System.out.println(shortner.longToShort(longUrl1, currentDate)); 
        System.out.println(shortner.longToShort(longUrl2, currentDate)); 
        System.out.println(shortner.longToShort(longUrl3, currentDate)); 
    }
}
