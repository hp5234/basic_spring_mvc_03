package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final 로 정의된 인스턴스를 가지고 생성자를 만들어준다.
public class BasicItemController {

    private final ItemRepository itemRepository;

    /*
    // @RequiredArgsConstructor 에 의해 final 로 정의된 ItemRepository 를 사용해 생성자를 만들어준다.
    // 생성자 자동 주입 -> 생성자가 한개만 있으면 생략 가능
    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    */

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    // 상품 목록
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    // 상품 상세
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    // 상품 추가 페이지 호출
    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    // 상품 추가 -> @RequestParam 사용
    // @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam Integer price,
                       @RequestParam Integer quantity,
                       Model model){

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item", item);

        return "basic/item";
    }

    // 상품 추가 -> @ModelAttribute 사용
    // @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item
                            // Model model // @ModelAttribute 에 의해 model 자동 생성
    ){
        /*
        @ModelAttribute 에 의해 자동 생성
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
         */

        itemRepository.save(item);

        // @ModelAttribute("item") 으로 인해 아래 코드 자동 실행
        // model.addAttribute("item", item);

        return "basic/item";
    }

    // 상품 추가 -> @ModelAttribute 사용 + model에 넣을 이름 생략
    // @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item
                            // @ModelAttribute 만 적을 경우
                            // 클래스 명 의 첫글자만 소문자로 바꾼 이름이 model 에 등록됨
                            // 지금의 경우 Item -> item
                            // Model model // @ModelAttribute 에 의해 model 자동 생성
    ){
        /*
        @ModelAttribute 에 의해 자동 생성
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
         */

        itemRepository.save(item);

        // @ModelAttribute("item") 으로 인해 아래 코드 자동 실행
        // model.addAttribute("item", item);


        return "basic/item";
    }

    // 상품 추가 -> @ModelAttribute 생략
    // @PostMapping("/add")
    public String addItemV4( Item item
                            // 단순 타입의 경우 @RequestParam 이 자동 적용되고
                            // 임의의 객체의 경우 @ModelAttribute 가 자동 적용된다.
    ){
        /*
        @ModelAttribute 에 의해 자동 생성
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
         */

        itemRepository.save(item);

        // @ModelAttribute("item") 으로 인해 아래 코드 자동 실행
        // model.addAttribute("item", item);

        return "basic/item";
    }

    // PRG 패턴을 적용
    // @PostMapping("/add")
    public String addItemV5( @ModelAttribute Item item
    ){
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    // RedirectAttributes 적용
    @PostMapping("/add")
    public String addItemV6(@ModelAttribute Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        // redirectAttributes 에 의해 { } 는 값으로 치환되고,
        // url 에 포함되지 않은 데이터는 쿼리파라미터 형식으로 ?status=true 형식처럼 뒤에 붙게 된다.
        return "redirect:/basic/items/{itemId}";
    }

    // 수정 페이지 호출
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    // 상품 수정 처리
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);

        // 리다이렉트
        return "redirect:/basic/items/{itemId}";
    }
}
