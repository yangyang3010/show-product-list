import { Component, OnInit } from '@angular/core';
import { ProductService } from '../app/product.service'; // Nhập dịch vụ
import { CommonModule } from '@angular/common'; // Nhập CommonModule

// var app = angular.module('app', []);
//
// app.controller('homeController', function($scope, $http) {
//   $scope.products = [];
//
//   // Fetch data from API
//   $http.get('http://localhost:8088/api/v1/products')
//     .then(function(response) {
//       $scope.products = response.data.products;
//     })
//     .catch(function(error) {
//       console.error('Failed to fetch products:', error);
//     });
// });

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule], // Thêm CommonModule vào đây
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit {
  products: any[] = []; // Biến lưu trữ danh sách sản phẩm
  filteredProducts: any[] = []; // Biến lưu trữ sản phẩm đã lọc
  categories: string[] = []; // Biến lưu trữ danh mục
  currentPage: number = 1; // Trang hiện tại
  totalPages: number = 1; // Tổng số trang
  searchQuery: string = ''; // Chuỗi tìm kiếm
  selectedCategory: string = ''; // Danh mục đã chọn

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.fetchProducts(); // Gọi hàm fetchProducts khi component được khởi tạo
  }

  fetchProducts() {
    this.productService.getProducts().subscribe(
      (data) => {
        console.log('Fetched products:', data.products); // Thêm log để kiểm tra dữ liệu
        this.products = data.products; // Gán dữ liệu sản phẩm
        this.filteredProducts = this.products; // Khởi tạo sản phẩm đã lọc
        this.categories = [...new Set(this.products.map(product => product.category))]; // Lấy danh sách danh mục
        this.totalPages = Math.ceil(this.products.length / 10); // Tính số trang
      },
      (error) => {
        console.error('Failed to fetch products:', error); // Xử lý lỗi
      }
    );
  }

  onSearchChange(event: Event) {
    const inputElement = event.target as HTMLInputElement; // Ép kiểu cho event.target
    this.searchQuery = inputElement.value; // Lấy giá trị tìm kiếm
    this.filterProducts(); // Gọi phương thức lọc khi giá trị tìm kiếm thay đổi
  }

  onCategoryChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement; // Ép kiểu cho event.target
    this.selectedCategory = selectElement.value; // Lấy giá trị danh mục đã chọn
    this.filterProducts(); // Gọi phương thức lọc khi danh mục thay đổi
  }

  filterProducts() {
    // Logic cho bộ lọc
    this.filteredProducts = this.products.filter(product => {
      const matchesSearchQuery = this.searchQuery
        ? product.name.toLowerCase().includes(this.searchQuery.toLowerCase())
        : true; // Nếu không có truy vấn tìm kiếm, thì cho qua

      const matchesCategory = this.selectedCategory
        ? product.category === this.selectedCategory // Giả sử mỗi sản phẩm có thuộc tính category
        : true; // Nếu không có danh mục đã chọn, thì cho qua

      return matchesSearchQuery && matchesCategory; // Trả về sản phẩm khớp với cả hai điều kiện
    });

    // Cập nhật lại tổng số trang
    this.totalPages = Math.ceil(this.filteredProducts.length / 10);
    this.currentPage = 1; // Đặt lại trang hiện tại về 1 khi lọc
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }
}
