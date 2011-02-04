package se.vgregion.verticalprio.repository.finding;

import java.util.List;

/**
 * @author Claes Lundahl, vgrid=clalu4
 * 
 */
public interface HaveQuerySortOrder {

    List<SortOrderField> listSortOrders();

    public static class SortOrderField implements Comparable<SortOrderField> {

        /**
         * Name of the field.
         */
        private String name;

        /**
         * Should the values be sorted asc or desc?
         */
        private boolean ascending = true;

        /**
         * If several SortOrderField is used what order should they have?
         */
        private int order;

        /**
         * This is 'working memory' for the jpql producer (JpqlMatchBuilder).
         */
        private String alias;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isAscending() {
            return ascending;
        }

        public void setAscending(boolean ascending) {
            this.ascending = ascending;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getOrder() {
            return order;
        }

        /**
         * @inheritDoc
         */
        @Override
        public int compareTo(SortOrderField o) {
            return order - o.order;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getAlias() {
            return alias;
        }

    }

}
