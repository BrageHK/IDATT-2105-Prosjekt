package edu.ntnu.idatt2105.backend.DTO;

import edu.ntnu.idatt2105.backend.DTO.ProductDTO;
import jakarta.persistence.*;

@Entity(name = "product_images")
public class ProductImageDTO {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "image_url", nullable = false)
        private String imageUrl;

        @Column(name = "image_description", nullable = false)
        private String imageDescription;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id")
        private ProductDTO product;

        public ProductImageDTO() {
        }

        public String getImageDescription() {
            return imageDescription;
        }

        public void setImageDescription(String imageDescription) {
            this.imageDescription = imageDescription;
        }

        public ProductImageDTO(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public ProductDTO getProduct() {
            return product;
        }

        public void setProduct(ProductDTO product) {
            this.product = product;
        }

}
