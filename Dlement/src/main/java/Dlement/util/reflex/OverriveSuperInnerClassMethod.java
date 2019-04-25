package Dlement.util.reflex;

public class OverriveSuperInnerClassMethod extends SuperClass{

	public class Format{
		public void say() {
			// TODO Auto-generated method stub
			System.out.println(2);
		}
	}
	
	
	public static void main(String[] args) {
		
		SuperClass s = new OverriveSuperInnerClassMethod();
		s.a();
		
	}
	
}
