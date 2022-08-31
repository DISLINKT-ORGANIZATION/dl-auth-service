package dislinkt.authservice.repositories.specification;

import dislinkt.authservice.entities.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {

    private String query;

    public UserSpecification(String query) {
        this.query = query;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate p = cb.disjunction();
        p.getExpressions().add(cb.notEqual(cb.locate(cb.lower(root.get("username")), this.query.toLowerCase()), 0));
        p.getExpressions().add(cb.notEqual(cb.locate(cb.lower(root.get("email")), this.query.toLowerCase()), 0));
        p.getExpressions().add(cb.notEqual(cb.locate(cb.lower(root.get("firstName")), this.query.toLowerCase()), 0));
        p.getExpressions().add(cb.notEqual(cb.locate(cb.lower(root.get("lastName")), this.query.toLowerCase()), 0));

        return p;
    }

}
