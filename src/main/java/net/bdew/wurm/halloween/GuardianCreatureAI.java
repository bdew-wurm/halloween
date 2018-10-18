package net.bdew.wurm.halloween;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.WurmId;
import com.wurmonline.server.behaviours.CreatureBehaviour;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.Creatures;
import com.wurmonline.server.creatures.NoSuchCreatureException;
import com.wurmonline.server.creatures.ai.CreatureAI;
import com.wurmonline.server.creatures.ai.CreatureAIData;
import com.wurmonline.server.creatures.ai.PathTile;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.zones.NoSuchZoneException;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.CounterTypes;
import net.bdew.wurm.halloween.titles.CustomAchievements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GuardianCreatureAI extends CreatureAI {
    @Override
    protected boolean pollSpecialPreAttack(Creature c, long delta) {
        GuardianCreatureAIData data = GuardianCreatureAIData.get(c);
        if (data.guarded == null || data.guarded.deleted)
            return data.tryFindGuarded();
        return false;
    }

    @Override
    protected boolean pollMovement(Creature creature, long delta) {
        GuardianCreatureAIData aiData = GuardianCreatureAIData.get(creature);

        if (creature.getStatus().getPath() == null) {
            if (creature.getTarget() != null) {
                if (!creature.getTarget().isWithinDistanceTo(creature, 6.0F)) {
                    creature.startPathingToTile(this.getMovementTarget(creature, creature.getTarget().getTileX(), creature.getTarget().getTileY()));
                } else {
                    creature.setOpponent(creature.getTarget());
                }
            } else if (creature.getLatestAttackers().length > 0) {
                ArrayList<Creature> attackerList = new ArrayList<>();

                for (long atkId : creature.getLatestAttackers()) {
                    try {
                        attackerList.add(Creatures.getInstance().getCreature(atkId));
                    } catch (NoSuchCreatureException ignored) {
                    }
                }

                attackerList.sort((creature1, creature2) -> {
                    float distance1 = creature1.getPos2f().distance(creature.getPos2f());
                    float distance2 = creature2.getPos2f().distance(creature.getPos2f());
                    return Float.compare(distance1, distance2);
                });

                boolean gotTarget = false;

                while (!gotTarget && !attackerList.isEmpty()) {
                    Creature newTarget = attackerList.remove(0);
                    if (newTarget.isWithinDistanceTo(creature, (float) creature.getMaxHuntDistance())) {
                        creature.setTarget(newTarget.getWurmId(), true);
                        if (!creature.getTarget().isWithinDistanceTo(creature, 6.0F)) {
                            creature.startPathingToTile(this.getMovementTarget(creature, creature.getTarget().getTileX(), creature.getTarget().getTileY()));
                        }
                        gotTarget = true;
                    }
                }
            } else if (aiData.guarded.getPos2f().distance(creature.getPos2f()) > 10f) {
                if (aiData.pathAttempts++ > 5) {
                    float posX = (aiData.guarded.getTilePos().x + Server.rand.nextInt(5) - 2) * 4 + 2;
                    float posY = (aiData.guarded.getTilePos().y + Server.rand.nextInt(5) - 2) * 4 + 2;
                    try {
                        float posZ = Zones.calculateHeight(posX, posY, aiData.guarded.isOnSurface());
                        CreatureBehaviour.blinkTo(creature, posX, posY, aiData.guarded.isOnSurface() ? 0 : -1, posZ, -10L, 0);
                        aiData.pathAttempts = 0;
                    } catch (NoSuchZoneException e) {
                        Halloween.logException("Error calculating blink", e);
                    }
                } else {
                    creature.setPathfindcounter(0);
                    PathTile target = getMoveTargetNearGuarded(creature, aiData);
                    if (target != null)
                        creature.startPathingToTile(target);
                }
            } else {
                this.increaseTimer(creature, delta, 0);
                if (this.isTimerReady(creature, 0, 5000L) && Server.rand.nextInt(10) == 0) {
                    this.simpleMovementTick(creature);
                    this.resetTimer(creature, 0);
                }
            }
        } else {
            aiData.pathAttempts = 0;
            this.pathedMovementTick(creature);
            if (creature.getStatus().getPath().isEmpty()) {
                creature.getStatus().setPath(null);
                creature.getStatus().setMoving(false);
            }
        }

        return false;
    }

    private PathTile getMoveTargetNearGuarded(Creature creature, GuardianCreatureAIData data) {
        int tilePosX = data.guarded.getTilePos().x + Server.rand.nextInt(5) - 2;
        int tilePosY = data.guarded.getTilePos().y + Server.rand.nextInt(5) - 2;
        final int tile = Server.surfaceMesh.getTile(tilePosX, tilePosY);
        if (Tiles.decodeHeight(tile) > -creature.getHalfHeightDecimeters()) {
            return new PathTile(tilePosX, tilePosY, tile, creature.isOnSurface(), creature.getFloorLevel());
        }
        return null;
    }


    @Override
    protected boolean pollAttack(Creature creature, long delta) {
        return false;
    }

    @Override
    protected boolean pollBreeding(Creature creature, long delta) {
        return false;
    }

    @Override
    public boolean creatureDied(Creature creature) {
        Set<Player> killers = Arrays.stream(creature.getLatestAttackers())
                .filter(e -> WurmId.getType(e) == CounterTypes.COUNTER_TYPE_PLAYERS)
                .mapToObj(Players.getInstance()::getPlayerOrNull)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (creature.getTemplate().getTemplateId() == CustomCreatures.pumpkinId)
            killers.forEach(p -> CustomAchievements.triggerAchievement(p, CustomAchievements.pumpkinKiller));
        else if (creature.getTemplate().getTemplateId() == CustomCreatures.treeId)
            killers.forEach(p -> CustomAchievements.triggerAchievement(p, CustomAchievements.treeKiller));

        return false;
    }

    @Override
    public CreatureAIData createCreatureAIData() {
        return new GuardianCreatureAIData();
    }

    @Override
    public void creatureCreated(Creature creature) {
        creature.getCreatureAIData().setCreature(creature);
    }
}
