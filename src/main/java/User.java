import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class User {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/users";

    private static final Gson gson = new Gson();

    public static UserIdentity createUser(){
        try{
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            //AddressIdentity address1 = new AddressIdentity("Douglas Extension", "Suite 847", "McKenziehaven", "59590-4157", new GeoIdentity("-68.6102", "-47.0653"));

            String requestBody = "{\"name\":\"Sandra Wrona\", \"username\":\"sandraWr\",\"email\":\"sandrawrona@example.com\", \"address\":{\"street\":\"Wadowicka\",\"suite\":\"Suite 444\",\"city\":\"Warszawa\",\"zipcode\":\"30-457\",\"geo\":{\"lat\":\"-18.6142\",\"lng\":\"-22.0333\"}}}";
            try(OutputStream outputStream = connection.getOutputStream()){
                outputStream.write(requestBody.getBytes());
                outputStream.flush();
            }
            int responseCode = connection.getResponseCode();
            if(HttpURLConnection.HTTP_CREATED == responseCode){
                return getUserIdentity(connection);
            }
            connection.disconnect();
        } catch (IOException e){
            throw new RuntimeException("Something");
        }
       return null;
    }

    public static UserIdentity updateUser(int id) {
        try {
            URL url = new URL(BASE_URL + "/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String requestBody = "{\"id\":1,\"name\":\"Daria Wozniak\",\"username\":\"daria_w\",\"email\":\"dariawozniak@example.com\",\"address\":{\"street\":\"Koszykarska\",\"suite\":\"Suite 8\",\"city\":\"Krakow\",\"zipcode\":\"59590-4888\",\"geo\":{\"lat\":\"-48.0002\",\"lng\":\"-56.0363\"}}}";

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(requestBody.getBytes());
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                return getUserIdentity(connection);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean deleteUser(int id) {
        try {
            URL url = new URL(BASE_URL + "/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            return responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<UserIdentity> getUsers() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Type userListType = new TypeToken<List<UserIdentity>>() {
                    }.getType();
                    return gson.fromJson(response.toString(), userListType);
                }
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Optional<UserIdentity> getUserById(int id) {
        try {
            URL url = new URL(BASE_URL + "/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    UserIdentity user = gson.fromJson(response.toString(), UserIdentity.class);
                    return Optional.ofNullable(user);
                }
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static Optional<UserIdentity> getUserByUsername(String username) {
        try {
            URL url = new URL(BASE_URL + "?username=" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Type userListType = new TypeToken<List<UserIdentity>>() {
                    }.getType();
                    List<UserIdentity> users = gson.fromJson(response.toString(), userListType);
                    if (!users.isEmpty()) {
                        return Optional.of(users.get(0));
                    }
                }
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static void allCommentsToTheLastPost(int userId) {
        try {
            URL urlPosts = new URL(BASE_URL + "/" + userId + "/posts");
            HttpURLConnection postsConnection = (HttpURLConnection) urlPosts.openConnection();
            postsConnection.setRequestMethod("GET");

            int responseCodePosts = postsConnection.getResponseCode();
            if (responseCodePosts == HttpURLConnection.HTTP_OK) {
                try (BufferedReader readerPosts = new BufferedReader(new InputStreamReader(postsConnection.getInputStream()))) {
                    StringBuilder responsePosts = new StringBuilder();
                    String linePosts;
                    while ((linePosts = readerPosts.readLine()) != null) {
                        responsePosts.append(linePosts);
                    }

                    Type postListType = new TypeToken<List<Post>>() {}.getType();
                    List<Post> posts = gson.fromJson(responsePosts.toString(), postListType);
                    if (!posts.isEmpty()) {
                        Post latestPost = posts.get(posts.size() - 1);
                        int postId = latestPost.getId();

                        URL urlComments = new URL(BASE_URL + "/" + postId + "/comments");
                        HttpURLConnection commentsConnection = (HttpURLConnection) urlComments.openConnection();
                        commentsConnection.setRequestMethod("GET");

                        int commentsResponseCode = commentsConnection.getResponseCode();
                        if (commentsResponseCode == HttpURLConnection.HTTP_OK) {
                            try (BufferedReader readerComments = new BufferedReader(new InputStreamReader(commentsConnection.getInputStream()))) {
                                StringBuilder responseComments = new StringBuilder();
                                String lineComments;
                                while ((lineComments = readerComments.readLine()) != null) {
                                    responseComments.append(lineComments);
                                }

                                String fileName = "user-" + userId + "-post-" + postId + "-comments.json";
                                try (FileWriter fileWriter = new FileWriter(fileName)) {
                                    fileWriter.write(responseComments.toString());
                                    System.out.println("Comments for user " + userId + " and post " + postId + " saved to " + fileName);
                                }
                            }
                        } else {
                            System.out.println("Failed to retrieve comments for user " + userId + " and post " + postId + ". Response Code: " + commentsResponseCode);
                        }
                        commentsConnection.disconnect();
                    } else {
                        System.out.println("No posts found for user " + userId);
                    }
                }
            } else {
                System.out.println("Failed to retrieve posts for user " + userId + ". Response Code: " + responseCodePosts);
            }
            postsConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void getOpenTasksForUser(int userId) {
        try {
            URL url = new URL(BASE_URL + "/" + userId + "/todos");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    Type todoListType = new TypeToken<List<Todo>>() {}.getType();
                    List<Todo> todos = gson.fromJson(response.toString(), todoListType);

                    System.out.println("Open tasks for user " + userId + ":");
                    for (Todo todo : todos) {
                        if (!todo.isCompleted()) {
                            System.out.println("- " + todo.getTitle());
                        }
                    }
                }
            } else {
                System.out.println("Failed to get tasks for user " + userId + ". Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static UserIdentity getUserIdentity(HttpURLConnection connection) throws IOException{
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            return gson.fromJson(builder.toString(), UserIdentity.class);
        }
    }

}
