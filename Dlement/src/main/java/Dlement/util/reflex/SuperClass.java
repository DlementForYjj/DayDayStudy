package Dlement.util.reflex;

public class SuperClass {

	public class Format{
		
		public void say(){
			System.out.println(1);
		}
		
	}
	
	public void a(){
		Format f = new Format();
		f.say();
	}
}
