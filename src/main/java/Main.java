public class Main {
    public static void main(String[] args) {
        System.out.println(User.createUser());
        System.out.println(User.updateUser(1));
        System.out.println(User.deleteUser(2));
        System.out.println(User.getUsers());
        System.out.println(User.getUserById(3));
        System.out.println(User.getUserByUsername("daria_w"));

        User.allCommentsToTheLastPost(1);

        User.getOpenTasksForUser(1);
    }
}
