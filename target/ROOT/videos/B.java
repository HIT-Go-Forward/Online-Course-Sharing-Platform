public class B
{
    public static B t1 = new B();
    public static B t2 = new B();
    {
        System.out.println("constructure");
    }
    static
    {
        System.out.println("static");
    }
    public static void main(String[] args)
    {
        B t = new B();
    }
}