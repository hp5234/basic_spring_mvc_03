package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// @Data -> 기능을 포괄적으로 들고오므로 핵심 도메인에서 사용하기 위험하다.
// 사용하기 위해선 내용을 반드시 알고 사용할 것
// @Getter @Setter
@Data
public class Item {

    private Long id;
    private String itemName;

    // 값이 안들어갈 수 도 있는 상황을 위해 Integer 사용 -> null
    private Integer price; // 가격
    private Integer quantity; // 수량

    // 기본 생성자
    public Item() {
    }

    // id 를 제외한 생성자
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
