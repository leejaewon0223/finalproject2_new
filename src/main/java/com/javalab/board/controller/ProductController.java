package com.javalab.board.controller;

import com.javalab.board.dto.BasketDTO;

import com.javalab.board.dto.BasketProductDTO;
import com.javalab.board.dto.ProductDTO;
import com.javalab.board.entity.Category;
import com.javalab.board.entity.Product;
import com.javalab.board.service.CategoryService;
import com.javalab.board.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private CategoryService categoryService;
    
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // 상품목록 가져오기
    @GetMapping
    public String getAllProducts(Model model, 
    		@PageableDefault(page=0, size=6, sort="productNo", direction = Sort.Direction.DESC) Pageable pageable,
    		String searchKeyword) {
    	 
    	Page<Product> products = null;
    	
    	if(searchKeyword == null) {
    		products = productService.getAllProducts(pageable);
    	} else {
    		products = productService.productSearchList(searchKeyword, pageable);
    	}
    	
        
       
        int nowPage = products.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, products.getTotalPages());
        
        model.addAttribute("products", products);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        
      
        return "product/allProducts";
    }
    
    
    
    // 상품 상세보기
    @GetMapping("/{productNo}")
    public String productDetail(@PathVariable Integer productNo, Model model, BasketDTO basketDTO, BasketProductDTO basketProductDTO) {
       ProductDTO product = productService.getProductDetail(productNo);
       model.addAttribute("product", product);
       return "product/productDetails";
    }
    
    // 상품 추가 페이지로 이동 어드민만
    @GetMapping("/add")
    public String showAddProductForm(Model model, HttpSession session) {
        boolean isAdmin = (boolean) session.getAttribute("isAdmin");
        if (isAdmin) {
            model.addAttribute("product", new Product());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/addProduct";
        } else {
            // Handle the case when the user is not an admin (e.g., show an error page, redirect to a different page)
            return "redirect:/products";
        }
    }

    // 상품추가
    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam("categoryId") int categoryId, 
    		List<MultipartFile> file) throws Exception {
        Category category = categoryService.getCategoryById(categoryId);
        product.setCategory(category);
        productService.saveProduct(product, file);
       
        
        return "redirect:/products";
    }
    // 상품 수정 페이지로 이동
    @GetMapping("/{productId}/edit")
    public String showEditProductForm(@PathVariable int productId, Model model) {
        Product product = productService.getProductById(productId);
        List<Category> categories = categoryService.getAllCategories(); // Retrieve the category list
        model.addAttribute("product", product);
        model.addAttribute("categories", categories); // Add the category list to the model
        return "product/editProduct";
    }
    
    // 상품추가 기능
    @PostMapping("/{productId}/edit")
    public String updateProduct(@PathVariable int productId, @ModelAttribute("product") Product updatedProduct) {
        productService.updateProduct(productId, updatedProduct);
        return "redirect:/products";
    }

    // 상품 삭제
    @PostMapping("/{productId}/delete")
    public String deleteProduct(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return "redirect:/products";
    }
    
   
}