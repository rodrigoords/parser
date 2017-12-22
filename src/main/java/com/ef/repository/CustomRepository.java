package com.ef.repository;

import com.ef.model.BlockedIP;
import com.ef.model.WalletHubLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class CustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Value("${bulk_insert.batch_size:20}")
  private Integer batchSize;

  /**
   * Bulk to try better performance when insert a large amount of data.
   * Hibernate keep a flush of data when inserting so we are clearing this
   * flush to preserve memory.
   * @param entities
   * @param <T>
   */
  @Transactional
  public <T extends Persistable> Collection<T> bulkSave(final Collection<T> entities) {

    final List<T> savedEntities = new ArrayList<>(entities.size());
    int i =0;

    for (final T t : entities) {

      savedEntities.add(persistOrMerge(t));
      i++;
      if (i % batchSize == 0) {
        this.entityManager.flush();
        this.entityManager.clear();
      }
    }
    return savedEntities;
  }

  /**
   * Normally we can use Spring JPA implementation to make data base access, in this case i want to make a specific
   * query to search result, Spring JPA specification impl cant make easy the group by with having query so i choose
   * to use TypedQuert with one Criteria Query.
   * @param startDate
   * @Param endDate
   * @param threshold
   * @return
   */
  @SuppressWarnings("unchecked")
  @Transactional
  public List<BlockedIP> analysis(LocalDateTime startDate, LocalDateTime endDate, Long threshold){
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

    CriteriaQuery<BlockedIP> criteriaQuery = criteriaBuilder.createQuery(BlockedIP.class);

    Root<WalletHubLog> root = criteriaQuery.from(WalletHubLog.class);

    criteriaQuery.multiselect(root.get("ip"), criteriaBuilder.count(root.get("ip")));
    criteriaQuery.where(criteriaBuilder.between(root.get("date"), startDate, endDate));
    criteriaQuery.groupBy(root.get("ip"));
    criteriaQuery.having(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.count(root.get("ip")), threshold));

    Query query = entityManager.createQuery(criteriaQuery);

    return query.getResultList();
  }

  private <T extends Persistable> T persistOrMerge(final T t) {

    if(t.isNew()){
      this.entityManager.persist(t);
      return t;
    }else{
      return this.entityManager.merge(t);
    }
  }
}
