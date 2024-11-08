package com.test.simplecrud.specification;

import com.test.simplecrud.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class UserSpecification {

    private static final String LIKE = "%";

    public static Specification<User> filter( String filter){
        if(isNull(filter))  return Specification.where(null);

        var filterLike =  LIKE + filter + LIKE;

        return Specification.where(like("name", filterLike)
                .and(like("email", filterLike))
                .and(like("phone", filterLike)));
    }

    private static Specification<User> like(String attribute, String filter) {
        return (root, query, cb) -> cb.like(root.get(attribute), filter);
    }

}

