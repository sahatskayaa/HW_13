public class Main {
    public static void main(String[] args) {
        System.out.println(СrudObjectActions.createUser());
        System.out.println(СrudObjectActions.updateUser(1));
        System.out.println(СrudObjectActions.deleteUser(2));
        System.out.println(СrudObjectActions.getUsers());
        System.out.println(СrudObjectActions.getUserById(3));
        System.out.println(СrudObjectActions.getUserByUsername("daria_w"));
        СrudObjectActions.allCommentsToTheLastPost(1);
        СrudObjectActions.getOpenTasksForUser(1);
    }
}
