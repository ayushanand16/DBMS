package com.DBMS.project.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.DBMS.project.model.Admin;
import com.DBMS.project.model.Blogs;
import com.DBMS.project.model.Course;
import com.DBMS.project.model.Offer;
import com.DBMS.project.model.Product;

import com.DBMS.project.repository.AdminRepository;
import com.DBMS.project.repository.BlogsRepository;
import com.DBMS.project.repository.CourseRepository;
import com.DBMS.project.repository.OfferRepository;
import com.DBMS.project.repository.ProductRepository;
import com.DBMS.project.service.UserInfoDetails;

import org.springframework.ui.Model;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.core.io.Resource;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminRepository adminRepository;
    private final CourseRepository courseRepository;
    private final BlogsRepository blogsRepository;
    private final ProductRepository productRepository;
    private final OfferRepository offerRepository;
    private final ResourceLoader resourceLoader;

    public AdminController(AdminRepository adminRepository, CourseRepository courseRepository,
            BlogsRepository blogsRepository, ProductRepository productRepository, OfferRepository offerRepository,
            ResourceLoader resourceLoader) {
        this.adminRepository = adminRepository;
        this.courseRepository = courseRepository;
        this.blogsRepository = blogsRepository;
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
        this.resourceLoader = resourceLoader;
    }

    @Deprecated
    @GetMapping("/products")
    public String adminProducts(Model model, Authentication authentication) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        List<Product> products = adminRepository.findProducts(admin.get(0).getId());
        model.addAttribute("products", products);
        return "adminProducts";
    }

    @Deprecated
    @GetMapping("/courses")
    public String adminCourses(Model model, Authentication authentication) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        List<Course> courses = adminRepository.findCourses(admin.get(0).getId());
        model.addAttribute("courses", courses);
        model.addAttribute("admin", admin.get(0));
        return "adminCourses";

    }

    @Deprecated
    @GetMapping("/offers")
    public String adminOffer(Model model, Authentication authentication) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        List<Offer> offers = adminRepository.findOffers(admin.get(0).getId());
        model.addAttribute("offers", offers);
        return "adminOffers";
    }

    @Deprecated
    @GetMapping("/blogs")
    public String adminBlogs(Model model, Authentication authentication) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        List<Blogs> blogs = adminRepository.findBlogs(admin.get(0).getId());
        model.addAttribute("blogs", blogs);
        model.addAttribute("edit", false);
        model.addAttribute("admin", admin.get(0));
        return "adminBlogs";
    }

    @PostMapping("/courses/add")
    @Deprecated
    public String createCourse(@ModelAttribute Course course, Authentication authentication) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        course.setCreator(admin.get(0).getId());
        System.out.println(course);
        courseRepository.save(course);
        return "redirect:/admin/courses";
    }

    @PostMapping("/blogs/add")
    @Deprecated
    public String createBlog(@RequestParam String title, @RequestParam String description,
            @RequestParam MultipartFile imageLink, Authentication authentication) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        Blogs blog = new Blogs();
        blog.setWriter((admin.get(0).getId()));
        blog.setTitle(title);
        try {
            Resource resource = resourceLoader.getResource("classpath:");
            String absolutePath = resource.getFile().getAbsolutePath();
            absolutePath = absolutePath + "/static/images/";
            String fileName = System.currentTimeMillis() + "_" + imageLink.getOriginalFilename();
            blog.setImageLink(fileName);
            System.out.println(fileName);
            Path filePath = Paths.get(absolutePath, fileName);
            Files.write(filePath, imageLink.getBytes());
        } catch (Exception e) {
            System.out.print(e);
        }
        blog.setDescription(description);
        blogsRepository.save(blog);
        return "redirect:/admin/blogs";
    }

    @PostMapping("/products/add")
    @Deprecated
    public String addProduct(@RequestParam String name, @RequestParam Long cost, @RequestParam Long stocksAvailable,
            @RequestParam MultipartFile image, Authentication authentication) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        Product product = new Product();
        product.setName(name);
        product.setCost(cost);
        product.setStocksAvailable(stocksAvailable);
        product.setMaintainer(admin.get(0).getId());
        try {
            Resource resource = resourceLoader.getResource("classpath:");
            String absolutePath = resource.getFile().getAbsolutePath();
            absolutePath = absolutePath + "/static/images/";
            System.out.println(absolutePath + "/n/n/n/n/n/n");
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            product.setImage(fileName);
            System.out.println(fileName);
            Path filePath = Paths.get(absolutePath, fileName);
            Files.write(filePath, image.getBytes());
        } catch (Exception e) {
            System.out.print(e);
        }

        productRepository.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/offers/add")
    @Deprecated
    public String addOffer(@ModelAttribute Offer offer, Authentication authentication) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        offer.setMaintainer(admin.get(0).getId());
        offerRepository.save(offer);
        return "redirect:/admin/offers";
    }

    @PostMapping("/courses/delete/{id}")
    @Deprecated
    public String deleteCourse(@PathVariable Long id, Authentication authentication) {
        Optional<Course> course = courseRepository.getCourse(id);
        if (course.isPresent()) {
            List<Admin> creator = adminRepository.findUser(course.get().getCreator());
            UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
            List<Admin> admin = adminRepository.findByEmail(user.getUsername());
            if (!creator.equals(admin)) {
                return "redirect: /unauthorised";
            }
            courseRepository.deleteCourse(course.get());
        }
        return "redirect:/admin/courses";
    }

    @PostMapping("/offers/delete/{id}")
    @Deprecated
    public String deleteOffer(@PathVariable Long id, Authentication authentication) {
        Optional<Offer> offer = offerRepository.getOffer(id);
        if (offer.isPresent()) {
            List<Admin> maintainer = adminRepository.findUser(offer.get().getMaintainer());
            UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
            List<Admin> admin = adminRepository.findByEmail(user.getUsername());
            if (!admin.equals(maintainer)) {
                return "redirect:/unauthorised";
            }
            offerRepository.deleteOffer(offer.get());
        }
        return "redirect:/admin/offers";

    }

    @PostMapping("/products/delete/{id}")
    @Deprecated
    public String deleteProduct(@PathVariable Long id, Authentication authentication) {
        Optional<Product> product = productRepository.getProduct(id);
        if (product.isPresent()) {
            List<Admin> maintainer = adminRepository.findUser(product.get().getMaintainer());
            UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
            List<Admin> admin = adminRepository.findByEmail(user.getUsername());
            if (!admin.equals(maintainer)) {
                return "redirect:/unauthorised";
            }
            productRepository.deleteProduct(product.get());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/blogs/delete/{id}")
    @Deprecated
    public String deleteBlog(@PathVariable Long id, Authentication authentication) {
        Optional<Blogs> blog = blogsRepository.get(id);
        if (blog.isPresent()) {
            List<Admin> writer = adminRepository.findUser(blog.get().getWriter());
            UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
            List<Admin> admin = adminRepository.findByEmail(user.getUsername());
            if (!(writer.equals(admin))) {
                return "redirect:/unauthorised";
            }
            blogsRepository.delete(blog.get());
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/edit/{id}")
    @Deprecated
    public String editBlog(@PathVariable Long id, Authentication authentication, Model model) {
        Optional<Blogs> blog = blogsRepository.get(id);
        if (blog.isPresent()) {
            List<Admin> writer = adminRepository.findUser(blog.get().getWriter());
            UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
            List<Admin> admin = adminRepository.findByEmail(user.getUsername());
            if (!writer.equals(admin)) {
                return "redirect:/unauthorised";
            }
            model.addAttribute("blog", blog.get());
            model.addAttribute("edit", true);
            return "addEditBlogs";
        }
        return "notfound";
    }

    @GetMapping("/courses/edit/{id}")
    @Deprecated
    public String editCourses(@PathVariable Long id, Authentication authentication, Model model) {
        Optional<Course> course = courseRepository.getCourse(id);
        if (course.isPresent()) {
            List<Admin> writer = adminRepository.findUser(course.get().getCreator());
            UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
            List<Admin> admin = adminRepository.findByEmail(user.getUsername());
            if (!writer.equals(admin)) {
                return "redirect:/unauthorised";
            }
            model.addAttribute("course", course.get());
            model.addAttribute("edit", true);
            return "addEditCourse";
        }
        return "notfound";
    }

    @GetMapping("/products/edit/{id}")
    @Deprecated
    public String editProducts(@PathVariable Long id, Authentication authentication, Model model) {
        Optional<Product> product = productRepository.getProduct(id);
        if (product.isPresent()) {
            List<Admin> writer = adminRepository.findUser(product.get().getMaintainer());
            UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
            List<Admin> admin = adminRepository.findByEmail(user.getUsername());
            if (!(writer.equals(admin))) {
                return "redirect:/unauthorised";
            }
            model.addAttribute("product", product.get());
            model.addAttribute("edit", true);
            return "addEditProducts";
        }
        return "notfound";
    }

    @GetMapping("/offers/edit/{id}")
    @Deprecated
    public String editOffers(@PathVariable Long id, Authentication authentication, Model model) {
        Optional<Offer> offer = offerRepository.getOffer(id);
        if (offer.isPresent()) {
            List<Admin> writer = adminRepository.findUser(offer.get().getMaintainer());
            UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
            List<Admin> admin = adminRepository.findByEmail(user.getUsername());
            if (!writer.equals(admin)) {
                return "redirect:/unauthorised";
            }
            model.addAttribute("offer", offer.get());
            model.addAttribute("edit", true);
            return "addEditOffer";
        }
        return "notfound";
    }

    @GetMapping("/offers/add")
    @Deprecated
    public String addOffer(Model model, Authentication authentication) {
        model.addAttribute("offer", new Offer());
        model.addAttribute("edit", false);
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        model.addAttribute("admin", admin);
        return "addEditOffer";
    }

    @GetMapping("/blogs/add")
    @Deprecated
    public String addBlog(Model model, Authentication authentication) {
        model.addAttribute("blog", new Blogs());
        model.addAttribute("edit", false);
        return "addEditBlogs";
    }

    @GetMapping("/courses/add")
    @Deprecated
    public String addCourse(Model model, Authentication authentication) {
        model.addAttribute("course", new Course());
        model.addAttribute("edit", false);
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        model.addAttribute("admin", admin);
        return "addEditCourse";
    }

    @GetMapping("/products/add")
    @Deprecated
    public String addProduct(Model model, Authentication authentication) {
        model.addAttribute("product", new Product());
        model.addAttribute("edit", false);
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        model.addAttribute("admin", admin.get(0));
        return "addEditProducts";
    }

    @PostMapping("/blogs/edit/{id}")
    @Deprecated
    public String editBlog(@PathVariable Long id, @RequestParam String title, @RequestParam String description,
            @RequestParam MultipartFile imageLink, Authentication authentication) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        Blogs blog = new Blogs();
        blog.setWriter((admin.get(0).getId()));
        blog.setTitle(title);
        try {
            Resource resource = resourceLoader.getResource("classpath:");
            String absolutePath = resource.getFile().getAbsolutePath();
            absolutePath = absolutePath + "/static/images/";
            String fileName = System.currentTimeMillis() + "_" + imageLink.getOriginalFilename();
            blog.setImageLink(fileName);
            blog.setId(id);
            Path filePath = Paths.get(absolutePath, fileName);
            Files.write(filePath, imageLink.getBytes());
        } catch (Exception e) {
            System.out.print(e);
        }
        blog.setDescription(description);
        blogsRepository.edit(blog);
        return "redirect:/admin/blogs";
    }

    @PostMapping("/offers/edit/{id}")
    public String editOffer(@PathVariable Long id, @ModelAttribute Offer offer, Authentication authentication) {
        offerRepository.edit(offer);
        return "redirect:/admin/offers";
    }

    @Deprecated
    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, @RequestParam String name, @RequestParam Long cost,
            @RequestParam Long stocksAvailable, @RequestParam MultipartFile image, Authentication authentication) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        Product product = new Product();
        product.setName(name);
        product.setCost(cost);
        product.setStocksAvailable(stocksAvailable);
        product.setMaintainer(admin.get(0).getId());
        product.setId(id);
        try {
            Resource resource = resourceLoader.getResource("classpath:");
            String absolutePath = resource.getFile().getAbsolutePath();
            absolutePath = absolutePath+"/static/images/";
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(absolutePath, fileName);
            product.setImage(fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, image.getBytes());
        } catch (Exception e) {
            System.out.print(e);
        }
        productRepository.edit(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/courses/edit/{id}")
    public String editCourse(@PathVariable Long id, @ModelAttribute Course course, Authentication authentication) {
        courseRepository.edit(course);
        return "redirect:/admin/courses";

    }

}
