package com.ultreon.mods.chunkyguns.item.gun;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Optional;

public class GunHitscanHelper {

    public static HitResult getCollision(LivingEntity shooter, double maxDistance) {
        EntityHitResult entityHitResult = GunHitscanHelper.getEntityCollision(shooter, maxDistance);
        if (entityHitResult != null) {
            return entityHitResult;
        }

        return GunHitscanHelper.getBlockCollision(shooter, maxDistance > 32 ? 32 : maxDistance);
    }

    public static BlockHitResult getBlockCollision(LivingEntity shooter, double maxDistance) {
        Vec3d origin = shooter.getEyePos();
        Vec3d direction = shooter.getRotationVector().normalize();
        Vec3d destination = origin.add(direction.multiply(maxDistance));

        return shooter.getWorld().raycast(new RaycastContext(origin, destination, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.ANY, shooter));
    }

    private record CollisionInfo(LivingEntity entity, Vec3d hitLocation, double distance2) {}

    public static EntityHitResult getEntityCollision(LivingEntity shooter, double maxDistance) {
        Vec3d origin = shooter.getEyePos();
        Vec3d direction = shooter.getRotationVector().normalize();
        Vec3d destination = origin.add(direction.multiply(maxDistance));
        CollisionInfo closestCollision = null;

        // Get all LivingEntities in the box bound by the origin and the destination
        for (final LivingEntity entity : shooter.getWorld().getEntitiesByClass(LivingEntity.class, shooter.getBoundingBox().stretch(direction.multiply(maxDistance)), (e) -> true)) {
            // Casts a ray from origin to destination to see if any entitiy hitboxes intersect it
            Optional<Vec3d> entityCollisionLocation = entity.getBoundingBox().raycast(origin, destination);
            if (entityCollisionLocation.isPresent()) {
                // Entity found, add it to the result arraylist to be sorted
                double distance2 = origin.squaredDistanceTo(entityCollisionLocation.get());
                if (closestCollision == null || closestCollision.distance2 > distance2) {
                    closestCollision = new CollisionInfo(entity, entityCollisionLocation.get(), distance2);
                }
            }
        }

        if (closestCollision != null) {
            if (shooter.world.raycast(new RaycastContext(origin, closestCollision.hitLocation, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, shooter)).getType() == net.minecraft.util.hit.HitResult.Type.MISS) {
                return new EntityHitResult(closestCollision.entity, closestCollision.hitLocation);
            }
        }

        return null;
    }
}
