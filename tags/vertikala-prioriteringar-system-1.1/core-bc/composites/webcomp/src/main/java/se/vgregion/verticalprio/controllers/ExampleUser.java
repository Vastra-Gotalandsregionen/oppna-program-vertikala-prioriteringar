package se.vgregion.verticalprio.controllers;

import se.vgregion.verticalprio.entity.User;
import se.vgregion.verticalprio.repository.finding.HaveExplicitTypeToFind;
import se.vgregion.verticalprio.repository.finding.HaveOrderByPaths;
import se.vgregion.verticalprio.repository.finding.OrderByPath;

import java.util.ArrayList;
import java.util.List;

/**
     * An class to use as search criteria. Is used to provide sorting to the resulting listing.
     *
     * @author Claes Lundahl, vgrid=clalu4.VGREGION
     *
     */
    public class ExampleUser extends User implements HaveOrderByPaths, HaveExplicitTypeToFind {
        private List<OrderByPath> paths = new ArrayList<OrderByPath>();

        public ExampleUser() {
            super();
            paths.add(new OrderByPath("firstName"));
            paths.add(new OrderByPath("lastName"));
        }

        @Override
        public List<OrderByPath> paths() {
            return paths;
        }

        @Override
        public Class<?> type() {
            return User.class;
        }

    }