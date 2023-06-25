public class Main {
    public static void main(String[] args) {
        System.out.println(crudObjectActions.createUser());
        System.out.println(crudObjectActions.updateUser(1));
        System.out.println(crudObjectActions.deleteUser(2));
        System.out.println(crudObjectActions.getUsers());
        System.out.println(crudObjectActions.getUserById(3));
        System.out.println(crudObjectActions.getUserByUsername("daria_w"));
        crudObjectActions.allCommentsToTheLastPost(1);
        crudObjectActions.getOpenTasksForUser(1);
    }
}
