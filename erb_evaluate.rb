require 'open-uri'
require 'erb'
require 'ostruct'
require 'java'
require 'date'
require 'digest/md5'
require 'base64'
require 'clab_safe_ruby'

## MD5 class in ruby 193 does't exist ... instead use module Digest::MD5.hexdigest
class MD5
    def self.md5(*args)
        Digest::MD5.hexdigest(*args)
    end
end

module OldEngine
    def include_url(url)
        url.gsub!(' ', '%20')
        @content_cache ||= {}
        
        unless @content_cache.has_key?(url)
            # https supported by open (open-uri wrapper)
            @content_cache[url] = open(url).read
        end
        
        return @content_cache[url]
    end
    
    def include_url_if(url, field_name, field_value)
        if field_value === user_data[field_name]
            return include_url(url)
        else
            return ""
        end
    end
    
    def include_url_if_not(url, field_name, field_value)
        if ! (field_value === user_data[field_name])
            return include_url(url)
        else
            return ""
        end
    end
end

class ErbEvaluateImpl
  include ErbEvaluate
    
  ## template should be a Java InputStream
  def initialize(template)  
    str_template = template.to_io.read
    
    clab_safe_ruby = ClabSafeRuby.new
    clab_safe_ruby.whitelist_const('Base64')
    
    if clab_safe_ruby.erb_check(str_template)
        @erb = ERB.new(str_template)
    else
        raise "Unsafe ERB code detected"
    end
  end
  
  ## Renders an ERB template against a hashmap of variables.
  def eval(variables)
    context = OpenStruct.new(variables).instance_eval do
        variables.each do |k, v|
            instance_variable_set(k, v) if k[0] == "@"
        end
        
        def get_user_data(key)
            @user_data[key] || ""
        end
        
        def user_data
            @user_data
        end
        
        def get_static_data(key)
            @static_data[key] || ""
        end
        
        def static_data
            @static_data
        end 
        
        def customer_id
            @customer_id
        end    
        
        self.extend OldEngine
        
        binding
    end
    
    @erb.result(context)
  end  
end

class RubyRunnerImpl
  include RubyRunner
    
  ## template should be a Java InputStream
  def initialize(code)  
    str_code = code.to_io.read
    
    clab_safe_ruby = ClabSafeRuby.new
    clab_safe_ruby.whitelist_const('Base64')
    
    if clab_safe_ruby.code_check(str_code)
	@str_code = str_code
    else
	raise "Unsafe ruby code detected"
    end
  end
  
  ## Renders an Ruby code against a hashmap of variables.
  ## code should be a Java InputStream
  def run(code, variables)
    context = OpenStruct.new(variables).instance_eval do
	variables.each do |k, v|
	    instance_variable_set(k, v) if k[0] == "@"
	end
	
	def get_user_data(key)
	    @user_data[key] || ""
	end
	
	def user_data
	    @user_data
	end
	
	def get_static_data(key)
	    @static_data[key] || ""
	end
	
	def static_data
	    @static_data
	end 
	
	def customer_id
	    @customer_id
	end    
	
	self.extend OldEngine
	
	self
    end

    context.instance_eval(@str_code).to_s
  end
end

