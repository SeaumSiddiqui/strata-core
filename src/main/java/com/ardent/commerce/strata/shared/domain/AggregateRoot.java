package com.ardent.commerce.strata.shared.domain;

/**
 * Marker class for Aggregate Roots.
 * Primary entry point to a cluster of domain objects.
 * Responsible for maintaining the consistency of the entire aggregate.
 */
public abstract class AggregateRoot extends Entity{
    // Inherits everything from Entity.
}
