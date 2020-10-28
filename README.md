# AutoComparable

## Code Sample

MyClass.java

```java
@AutoComparable
public class MyClass { // You don't need to implement 'Comparable<MyClass>'
	
	@AutoComparableTarget( priority = 1, order = Order.DESC )
	private int a;
		
	@AutoComparableTarget( priority = 2 )
	private int b;
	
	private String s;
	
	public MyClass( int a, int b, String s ) {
		this.a = a;
		this.b = b;
		this.s = s;
	}
	
	@Override
	public String toString() {
		return "(" + a + "," + b + "," + s + ")";
	}
}
```

Main.java

```java
public class Main {

	public static void main(String[] args) {
		List<MyClass> list = new ArrayList<MyClass>();
		list.add(new MyClass(3,3,"A"));
		list.add(new MyClass(2,2,"B"));
		list.add(new MyClass(14,3,"C"));
		list.add(new MyClass(2,7,"D"));
		list.add(new MyClass(6,6,"E"));
		list.add(new MyClass(8,7,"F"));
		list.add(new MyClass(2,1,"G"));
		list.add(new MyClass(6,11,"H"));
		
		System.out.print("Before : ");
		System.out.println(list);
		
		Collections.sort(list);
		
		System.out.print("After : ");
		System.out.println(list);
	}
}
```

Expected Result


```
Before : [(3,3,A), (2,2,B), (14,3,C), (2,7,D), (6,6,E), (8,7,F), (2,1,G), (6,11,H)]
After  : [(14,3,C), (8,7,F), (6,6,E), (6,11,H), (3,3,A), (2,1,G), (2,2,B), (2,7,D)]
```

...
...

Under Construction
