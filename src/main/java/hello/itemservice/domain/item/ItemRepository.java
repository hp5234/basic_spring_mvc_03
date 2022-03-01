package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository // 컴포넌트 스캔의 대상이 된다.
public class ItemRepository {

    // 주의 1. static 사용
    // 주의 2. 실제로는 HashMap 을 사용하면 안된다.
    // -> 싱글톤으로 생성되기 때문에 멀티 쓰레드 환경에서 여러개가 동시에 store 에 접근하는 환경의 경우 HashMap 을 사용하면 안된다.
    // ConcurrentHashMap<>() 을 사용하던 해야한다.
    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L; // static

    // 기능

    public Item save( Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById( Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        // store.values() 만으로 반환해도 되나,
        // 한번 감싸서 반환하게 되면 ArrayList<> 에 변경을 가해도
        // 실제 store 에는 영향이 없는 효과를 얻을 수 있다.
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updatePram){
        Item findItem = findById(itemId);

        // 정석은 하위 3개를 위한 별도의 DTO 객체를 만들어 사용하는것이 맞다. -> id 가 사용되지 않기 때문
        // ex ) ItemParamDTO  // 인스턴스 : itemName, price, quantity
        findItem.setItemName(updatePram.getItemName());
        findItem.setPrice(updatePram.getPrice());
        findItem.setQuantity(updatePram.getQuantity());
    }

    // 테스트에서 사용하기 위한 HashMap 데이터 전체 삭제를 위함
    public void clearStore() {
        store.clear();
    }
}
