package com.github.customentitylibrary.pathfinders;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.Random;

public class PathfinderCustomArrowAttack extends PathfinderBase
{
	private EntityInsentient entity;	//Entity executing this pathfinder
    //This decrements to 0 while the target is in range. When it reaches 0, the entity fires, and this goes back up to attackSpeed
    private int rangedAttackTime;
    private float speed;//Speed
    private int f = 0;	//Not sure
    private int attackSpeed;	//Attack speed
    private int maxRange;
    private int minRange;
	private String projectileName;

	private CustomFireProjectile customFire = null;

	Random rnd = new Random();

	public PathfinderCustomArrowAttack(EntityInsentient entity, float speed, int attackSpeed, int maxRange, int minRange)
	{
		this(entity, speed, attackSpeed, maxRange, minRange, "Arrow");
	}

	public PathfinderCustomArrowAttack(EntityInsentient entity, float speed, int attackSpeed, int maxRange, int minRange, CustomFireProjectile customFire)
	{
		this(entity, speed, attackSpeed, maxRange, minRange, "Arrow");
		this.customFire = customFire;
	}

	public PathfinderCustomArrowAttack(EntityInsentient entity, float speed, int attackSpeed, int maxRange, int minRange, String projectileName)
	{
		this.entity = entity;
		this.speed = speed;
		this.attackSpeed = attackSpeed;
		this.rangedAttackTime = attackSpeed;
		this.maxRange = maxRange;
		this.minRange = minRange;
		this.projectileName = projectileName;
		this.a(3);
	}
    
	@Override
	public boolean shouldExecute()
	{
		EntityLiving target = this.entity.getGoalTarget();
		if (target == null)
			return false;
		Vec3D entityLocation = Vec3D.a(entity.locX, entity.locY, entity.locZ);
		Vec3D targetLocation = Vec3D.a(target.locX, target.locY, target.locZ);
		return entityLocation.distanceSquared(targetLocation) >= minRange * minRange;
	}

	@Override
	public boolean continueExecuting()
	{
		EntityLiving target = this.entity.getGoalTarget();
		if(target == null)
			return false;
		Vec3D entityLocation = Vec3D.a(entity.locX, entity.locY, entity.locZ);
    	Vec3D targetLocation = Vec3D.a(target.locX, target.locY, target.locZ);
    	if(entityLocation.distanceSquared(targetLocation) < minRange*minRange)
    		return false;
        return this.a() || !this.entity.getNavigation().g();
	}

	@Override
	public void startExecuting()
	{
		
	}

	@Override
	public void stopExecuting()
	{
		EntityLiving target = entity.getGoalTarget();
		// CraftBukkit start
        EntityTargetEvent.TargetReason reason = (target == null || target.isAlive()) ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
        CraftEventFactory.callEntityTargetEvent(entity, null, reason);
        // CraftBukkit end
        this.f = 0;
	}

	@Override
	public void update()
	{
		EntityLiving target = entity.getGoalTarget();
		if(target == null) return;
        double d0 = this.entity.e(target.locX, target.boundingBox.b, target.locZ);
        boolean canSee = this.entity.getEntitySenses().canSee(target);

        if (canSee) {
            ++this.f;
        } else {
            this.f = 0;
        }

        if (d0 <= (double) maxRange*maxRange && this.f >= 20) {
            entity.getNavigation().g();
        } else {
            entity.getNavigation().a(target, this.speed);
        }

        float f1 = MathHelper.sqrt(d0) / 10f;
        if (f1 > 1.0F) {
            f1 = 1.0F;
        }
        
        this.entity.getControllerLook().a(target, 30.0F, 30.0F);
        this.rangedAttackTime = this.rangedAttackTime - 1;
        if (this.rangedAttackTime <= 0) {
            if (d0 <= (double) maxRange*maxRange && canSee) {
				if(customFire != null)
					customFire.fireProjectile(target, f1);
				else
					fireProjectile(target, f1, projectileName);
                this.rangedAttackTime = this.attackSpeed;	//Reset timer to attack speed
            }
        }
	}

	public void fireProjectile(EntityLiving target, float f, String name)
	{
		EntityType type = EntityType.valueOf(name.toUpperCase().replaceAll(" ", "_"));
		switch (type)
		{
			case ARROW:
				EntityArrow entityarrow = new EntityArrow(entity.world, entity, target, 1.6F, 12.0F);
				int eDamage = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, entity.getEquipment()[0]);
				int eKnockback = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, entity.getEquipment()[0]);

				//entityarrow.e() gets the current damage, entityarrow.b(double damage) sets damage
				//Check both on updates
				if (eDamage > 0)
					entityarrow.b(entityarrow.e() + (double) eDamage * 0.5D + 0.5D);

				if (eKnockback > 0)
					entityarrow.a(eKnockback);

				if (EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, entity.getEquipment()[0]) > 0)
					entityarrow.setOnFire(100);

				entity.makeSound("random.bow", 1.0F, 1.0F / (rnd.nextFloat() * 0.4F + 0.8F));
				entity.world.addEntity(entityarrow);
				break;
			case SNOWBALL:
				EntitySnowball entitysnowball = new EntitySnowball(entity.world, entity);
				double d0 = target.locX - entity.locX;
				double d1 = target.locY + target.getHeadHeight() - 1.100000023841858D - entitysnowball.locY;
				double d2 = target.locZ - entity.locZ;
				float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;

				entitysnowball.shoot(d0, d1 + f1, d2, 1.6F, 12.0F);
				entity.makeSound("random.bow", 1.0F, 1.0F / (rnd.nextFloat() * 0.4F + 0.8F));
				entity.world.addEntity(entitysnowball);
				break;
			case FIREBALL:
				double d5 = target.locX - entity.locX;
				double d6 = target.boundingBox.b + target.length / 2.0F - (entity.locY + entity.length / 2.0F);
				double d7 = target.locZ - entity.locZ;

				entity.world.a(null, 1008, (int)entity.locX, (int)entity.locY, (int)entity.locZ, 0);
				EntityLargeFireball entitylargefireball = new EntityLargeFireball(entity.world, entity, d5, d6, d7);

				entitylargefireball.yield = (int) (entitylargefireball.bukkitYield = 1);
				double d8 = 4.0D;
				Vec3D vec3d = entity.j(1.0F);

				entitylargefireball.locX = (entity.locX + vec3d.c * d8);
				entitylargefireball.locY = (entity.locY + entity.length / 2.0F + 0.5D);
				entitylargefireball.locZ = (entity.locZ + vec3d.e * d8);
				entity.world.addEntity(entitylargefireball);
				break;
			case SMALL_FIREBALL:
				d0 = target.locX - entity.locX + rnd.nextDouble() * 3;
				d1 = target.boundingBox.b + target.length / 2.0F - (entity.locY + entity.length / 2.0F);
				d2 = target.locZ - entity.locZ + rnd.nextDouble() * 3;
				f1 = MathHelper.c(f) * 0.5F;

				entity.world.a(null, 1009, (int)entity.locX, (int)entity.locY, (int)entity.locZ, 0);

				EntitySmallFireball entitysmallfireball = new EntitySmallFireball(entity.world, entity, d0 + rnd.nextGaussian() * f1, d1, d2 + rnd.nextGaussian() * f1);

				entitysmallfireball.locY = (entity.locY + entity.length / 2.0F + 0.5D);
				entity.world.addEntity(entitysmallfireball);
				break;
			case WITHER_SKULL:
				d0 = target.locX - entity.locX;
				d1 = target.boundingBox.b + target.length / 2.0F - (entity.locY + entity.length / 2.0F);
				d2 = target.locZ - entity.locZ;
				entity.world.a(null, 1014, (int)entity.locX, (int)entity.locY, (int)entity.locZ, 0);
				double d3 = entity.locX;
				double d4 = entity.locY + 3.0d;
				d5 = entity.locZ;
				d6 = d0 - d3;
				d7 = d1 - d4;
				d8 = d2 - d5;
				EntityWitherSkull entitywitherskull = new EntityWitherSkull(entity.world, entity, d6, d7, d8);

				if (rnd.nextFloat() < 0.001F) {
					entitywitherskull.a(true);
				}

				entitywitherskull.locY = d4;
				entitywitherskull.locX = d3;
				entitywitherskull.locZ = d5;
				entity.world.addEntity(entitywitherskull);
				break;
			case SPLASH_POTION:
				EntityPotion entitypotion = new EntityPotion(entity.world, entity, 32732);

				entitypotion.pitch -= -20.0F;
				d0 = target.locX + target.motX - entity.locX;
				d1 = target.locY + target.getHeadHeight() - 1.100000023841858D - entity.locY;
				d2 = target.locZ + target.motZ - entity.locZ;
				f1 = MathHelper.sqrt(d0 * d0 + d2 * d2);

				entitypotion.setPotionValue(16396);

				entitypotion.shoot(d0, d1 + f1 * 0.2F, d2, 0.75F, 8.0F);
				entity.world.addEntity(entitypotion);
				break;
			case EGG:
				EntityEgg egg = new EntityEgg(entity.world, entity);
				d0 = target.locX - entity.locX;
				d1 = target.locY + target.getHeadHeight() - 1.100000023841858D - egg.locY;
				d2 = target.locZ - entity.locZ;
				f1 = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;

				egg.shoot(d0, d1 + f1, d2, 1.6F, 12.0F);
				entity.makeSound("random.bow", 1.0F, 1.0F / (rnd.nextFloat() * 0.4F + 0.8F));
				entity.world.addEntity(egg);
				break;
			default:
				//What the hell are you trying to do, fire a creeper? Ya'll can't do that shit, try again
		}
	}

	public interface CustomFireProjectile
	{
		public void fireProjectile(EntityLiving target, float f);
	}
}
