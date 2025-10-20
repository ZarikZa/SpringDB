package com.example.project2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;





@Entity
@Table(name = "toilets")
public class Toilet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название обязательно")
    @Size(min = 2, max = 200, message = "Название должно быть от 2 до 200 символов")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Бренд обязателен")
    @Size(max = 100, message = "Бренд не должен превышать 100 символов")
    @Column(nullable = false)
    private String brand;

    @NotBlank(message = "Модель обязательна")
    @Size(max = 100, message = "Модель не должна превышать 100 символов")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Цвет обязателен")
    @Size(max = 50, message = "Цвет не должен превышать 50 символов")
    @Column(nullable = false)
    private String color;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Количество на складе обязательно")
    @Min(value = 0, message = "Количество не может быть отрицательным")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @NotBlank(message = "Материал обязателен")
    @Size(max = 100, message = "Материал не должен превышать 100 символов")
    @Column(nullable = false)
    private String material;

    @NotNull(message = "Категория обязательна")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "water_saving")
    private boolean waterSaving = false;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "toilet")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Toilet() {}

    public Toilet(String name, String brand, String model, String color, BigDecimal price,
                  Integer stockQuantity, String material, Category category, boolean waterSaving) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.material = material;
        this.category = category;
        this.waterSaving = waterSaving;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public boolean isWaterSaving() { return waterSaving; }
    public void setWaterSaving(boolean waterSaving) { this.waterSaving = waterSaving; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}