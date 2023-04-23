package jpabook.jpashop.service;


import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception{
        //JPA를 통해 데이터를 읽어와 객체로 만들고
        Book book = em.find(Book.class, 1L);
        //데이터를 변경한 후
        book.setName("test");
        //commit을 하면 JPA에서 데이터 변경을 감지하여 Update를 자동으로 진행함 -> Dirty Checking(변경감지)


        //그러나 준영속 엔티티는 JPA 영속성 컨텍스트가 관리하지 않아서 자동으로 JPA에서 처리하지 않는다
        //예를 들어 웹프론트에서 전달된 데이터를 수정하는 것은 이미 DB에 데이터가 있어도 JPA에 의해서 객체화된 데이터가 아니므로 준영속 엔티티에 해당한다.

        /*
        프론트에서 전달된 form 객체로부터 데이터를 받아서 Book객체를 수정할 때 Book 객체는 DB에 이미 있는 데이터여도
        JPA에 의해서 만들어지지 않았으므로 준영속 엔티티에 해당한다.
        따라서 마지막에 saveItem(merge)을 호출하여 저장하는 작업을 통해서 데이터 변경을 반영해야한다.

        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book); // merge호출

        **merge를 하게 되면 전달 받은 값으로 JPA가 DB에서 데이터를 찾아봐 값을 변경한 후 저장한다.
        파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회하고 없으면 DB에서 값을 찾는다
        이후 데이터 수정 후 다시 저장하고 해당 엔티티를 반환한다.
        반환된 객체는 영속성 컨텍스트에서 관리가 된다.
        다만 merge의 경우 파라미터로 넘어온 값에 변경을 의도한 값만 존재하여 변경을 원하지 않는 값이 null로된 상태로 넘어 온다면
        의도하지 않게 해당 값들을 null로 변경한다.
        즉 merge를 할 경우 변경하려고 하는 값뿐만아닌 다른 값들도 넘겨줘야한다.




        아래와 같이 JPA로 id에 해당하는 데이터를 가져온 후 값을 변경하면 JPA에 의해서 관리할 수 있다.
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        ((Book) findItem).setIsbn(param.getIsbn());
        ((Book) findItem).setAuthor(param.getAuthor());

        merge랑 비교했을 땐 영속성 컨택스트로 관리되는 객체를 반환하지 않고, 필요한 값만 변경가능하다.



        */

    }
}
