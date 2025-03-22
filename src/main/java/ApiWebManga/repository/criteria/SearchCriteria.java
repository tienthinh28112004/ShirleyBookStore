package ApiWebManga.repository.criteria;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCriteria {
    private String key;//tên của các cột
    private String operation;//>,<,>=,<=
    private Object value;//có nhiều dạng nên ta để Object
}
