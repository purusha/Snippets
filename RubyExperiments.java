import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.bsf.BSFManager;


public class RubyExperiments {
	private static final String CICCIO_CALIPPO = "ciccio calippo";

	public static void main(String[] args) throws Exception {	
		String string = org.apache.commons.io.IOUtils.toString(new InputStreamReader(RubyExperiments.class.getClassLoader().getResourceAsStream("erb_evaluate.rb")));
//		System.out.println(string);
		        
		//XXX 1 volta allo startup dell'applicazione
		BSFManager.registerScriptingEngine("jruby", "org.jruby.embed.bsf.JRubyEngine", new String[] {"rb"});
		BSFManager manager = new BSFManager();    		
		manager.exec("ruby", "<script>", 0, 0, string);
		
		//XXX 1 volta per tag/delivery
		String template = "<%= name %>";    		
		ErbEvaluate erbEval = (ErbEvaluate) manager.eval("ruby", "<script>", -1, -1, "ErbEvaluateImpl.new(\"" + template + "\")");
		//System.out.println(erbEval);
			
		//XXX 1 volta per recipient		
		final long before = System.currentTimeMillis();    		    		
		IntStream
			.range(0, 100_000)
			.parallel()		// USING PARALLELLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL UAHAHHAHAHA
			.forEach(i -> {
				try {
					final Map<String,Object> m = new HashMap<>();    	
					m.put("name", CICCIO_CALIPPO);
			    		        	        	
					erbEval.eval(m);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		long after = System.currentTimeMillis();
		System.out.println("elpsed: " + (after - before) + " millis.\n");
	}

//	public static void main1(String[] args) throws Exception {
//		final ScriptEngine jruby = new ScriptEngineManager().getEngineByName("jruby");
//		jruby.eval("require 'erb'");
//		
//		//String line = "<%= get_user_data(\"EMAIL\") %>";
//		String line = "<%= name %>";
//		
//		CompiledScript compile = ((Compilable) jruby).compile(
//"				class ERBEvaluator					\n" +
//"				  def init(template)				\n" +
//"				    this.erb = ERB.new(template)	\n" +	
//"				  end								\n" +
//"													\n" +
//"				  def eval							\n" +
//"				  end								\n" +
//"				end									\n"
//		);
//		
//    	final Map<String,Object> m = new HashMap<>();    	
//    	m.put("name", CICCIO_CALIPPO);
//				
//		Bindings bindings = new SimpleBindings(m);
//		
//		SimpleScriptContext scriptContext = new SimpleScriptContext();
//		scriptContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
//		
//		String valueOf = String.valueOf(compile.eval(scriptContext));
//		System.out.println(valueOf);
//		
//		//final Ruby runtime = Ruby.newInstance();		
//	}

}
