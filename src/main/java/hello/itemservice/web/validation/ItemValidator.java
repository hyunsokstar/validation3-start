package hello.itemservice.web.validation;
import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


// Validator 를 구현 하자
// 1. 오버라이드 메서드로 supports() , validate(Object target, Errors errors) 구현 하기
// 2. supports() : 해당 클래스 적격 여부 검증 => 이후 자세히 설명
// 3. validate 유효성 검사 로직 실행
@Component // ValidationItemControllerV2 에서 사용하기 위해 빈 등록
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    // target 은 타겟 객체 errors
    // bindingReusult 객체를 받아온다.(Errors 는 bindingReusult의 부모 클래스 이므로 가능)
    @Override
    public void validate(Object target, Errors errors) {
        // item 객체를 다음과 같이 생성
        Item item = (Item) target;
        
        // 검증 로직을 복붙 + bindingResult 를 errors로 수정
        if (!StringUtils.hasText(item.getItemName())) {
            errors.rejectValue("itemName", "required");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

    }
}
