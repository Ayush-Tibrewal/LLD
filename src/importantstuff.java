// error handling very important
import java.util.*; 
import java.util.Scanner;
class Exceptionss {
    
  void takevalue(int a) throws Exception {
    if (a == 0) {
        throw new Exception("number can'nt be zer0");
    } else {
        System.out.println("do northing");
    }
}
class Main {
    public static void main(String[] args) {
       Exceptionss exp = new Exceptionss();
        try {
            exp.takevalue(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
// ---------------------------------------

// factory design pattern 
interface Shape {
    void draw();
}

class Circle implements Shape {
    public void draw() {
        System.out.println("Drawing a Circle");
    }
}

class Square implements Shape {
    public void draw() {
        System.out.println("Drawing a Square");
    }
}

class ShapeFactory {
    public Shape getShape(String type) {
        if (type == null) return null;
        if (type.equalsIgnoreCase("circle")) return new Circle();
        else if (type.equalsIgnoreCase("square")) return new Square();
        return null;
    }
}

public class Main {
    public static void main(String[] args) {
        ShapeFactory factory = new ShapeFactory();
        Shape s1 = factory.getShape("circle");
        s1.draw();
        Shape s2 = factory.getShape("square");
        s2.draw();
    }
}

// =-======================================

//Builder design patterns 
class Student {
    String name;
    int age;
    String college;
    String address;

    Student(Builder b) {
        this.name = b.name;
        this.age = b.age;
        this.college = b.college;
        this.address = b.address;
    }

    static class Builder {
        String name;
        int age;
        String college;
        String address;

        Builder setName(String name) {
            this.name = name;
            return this;
        }

        Builder setAge(int age) {
            this.age = age;
            return this;
        }

        Builder setCollege(String college) {
            this.college = college;
            return this;
        }

        Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        Student build() {
            return new Student(this);
        }
    }

    void show() {
        System.out.println(name + " " + age + " " + college + " " + address);
    }
}

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student.Builder()
                        .setName("Ayush")
                        .setAge(21)
                        .build();

        Student s2 = new Student.Builder()
                        .setName("Ayush")
                        .setAge(21)
                        .setCollege("DTU")
                        .setAddress("Delhi")
                        .build();

        s1.show();
        s2.show();
    }
}
// ========================================================

// singleton method 
class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    void show() {
        System.out.println("Singleton instance working");
    }
}

public class Main {
    public static void main(String[] args) {
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();

        s1.show();
        System.out.println(s1 == s2);
    }
}

///////////////////////////////////////////////////////////
