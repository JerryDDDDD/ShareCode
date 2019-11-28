跨域这个问题，在开发中太过于常见了。传统的解决方案是 JSONP ，但是现在流行 RESTful 风格的应用，除了 GET 请求，还有PUT、POST、DELETE ，而 JSONP 只支持 GET 请求。

CORS（CORS，Cross-origin resource sharing）跨域源资源共享，是一个 W3C 标准，它是一份浏览器技术的规范，提供了 Web 服务从不同网域传来沙盒脚本的方法，以避开浏览器的同源策略，这是 JSONP 模式的现代版。

下面我将使用SpringBoot实现跨域请求。

##### 1. 首先需要一个demo1,demo1的端口号是8081

```
@Controller
public class HelloController {
    @ResponseBody
    @RequestMapping("/hello")
    @CrossOrigin(origins = "http://localhost:8082")
    public String  hello(){
        return "hello";
    }
}
```

##### 2. 然后需要新建一个项目demo2,demo2的端口号是8082

在static文件下新建index.html，因为我们需要用到ajax发送请求，所有请先将JQuerry下载放到static文件下并在index.html中引入。

```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Index</title>
    <script src="jquery-3.4.1.min.js"></script>
</head>
<body>
<div id="app"></div>
<input type="button" onclick="getData()" value="GET">
<script>
    function getData() {
        $.ajax({
            type:"GET",
            url:"http://localhost:8081/hello",
            success:function (res) {
                $("#app").html(res)
                alert(res);
            }

        })
    }
</script>
</body>
</html>
```
###### 另一种方法
上面再demo1中的hello方法上注解了`
 @CrossOrigin(origins = "http://localhost:8082")
`
，运行两个项目后，请求可以访问的，但是我们可以将代码完善一下，因为我们的项目中不可能只有一个方法是需要跨域访问的，难道我们每个方法都要注解上吗，当然SpringBoot支持将注解写在类上，下面提高另一种方法。

- 新建一个配置类WebMvcConfig.Java，注解 `@Configuration`,实现`WebMvcConfigurer `，重写`addCorsMappings`方法。


```
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8082")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(30*1000);
    }
}
```
