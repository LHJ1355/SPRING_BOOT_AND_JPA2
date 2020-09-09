package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Long saveItem(Item item){
        itemRepository.save(item);
        return item.getId();
    }

    public Item findItem(Long id){
        return itemRepository.findOne(id);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item updateItem(Long id, String name, int price, int stockQuantity){
        Item findItem =itemRepository.findOne(id);
        findItem.change(name, price, stockQuantity);
        return findItem;
    }
}
