package cn.lunadeer.liteworldedit;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Vector;

/**
 * Represents a cuboid (rectangular prism) in a 3D space.
 * This class provides methods to manipulate and query the cuboid's properties,
 * such as its dimensions, volume, and intersection with other cuboids.
 */
public class Cuboid {

    /**
     * A constant representing a zero-sized cuboid.
     */
    public static Cuboid ZERO = new Cuboid(0, 0, 0, 0, 0, 0);

    private int[] pos1 = new int[3];
    private int[] pos2 = new int[3];

    /**
     * Constructs a CuboidDTO with the specified positions.
     *
     * @param pos1 the first position of the cuboid
     * @param pos2 the second position of the cuboid
     */
    public Cuboid(int[] pos1, int[] pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        sortPos();
    }

    /**
     * Constructs a CuboidDTO with the same positions as the specified cuboid.
     *
     * @param cuboid the cuboid to copy positions from
     */
    public Cuboid(Cuboid cuboid) {
        this.pos1 = cuboid.getPos1().clone();
        this.pos2 = cuboid.getPos2().clone();
    }

    /**
     * Constructs a CuboidDTO with the specified coordinates.
     *
     * @param x1 the x-coordinate of the first position
     * @param y1 the y-coordinate of the first position
     * @param z1 the z-coordinate of the first position
     * @param x2 the x-coordinate of the second position
     * @param y2 the y-coordinate of the second position
     * @param z2 the z-coordinate of the second position
     */
    public Cuboid(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.pos1[0] = x1;
        this.pos1[1] = y1;
        this.pos1[2] = z1;
        this.pos2[0] = x2;
        this.pos2[1] = y2;
        this.pos2[2] = z2;
        sortPos();
    }

    /**
     * Constructs a CuboidDTO with the specified world and positions.
     *
     * @param pos1 the first position of the cuboid
     * @param pos2 the second position of the cuboid
     */
    public Cuboid(Vector<Integer> pos1, Vector<Integer> pos2) {
        this.pos1[0] = pos1.get(0);
        this.pos1[1] = pos1.get(1);
        this.pos1[2] = pos1.get(2);
        this.pos2[0] = pos2.get(0);
        this.pos2[1] = pos2.get(1);
        this.pos2[2] = pos2.get(2);
        sortPos();
    }

    /**
     * Constructs a CuboidDTO with the specified world and locations.
     *
     * @param loc1 the first location of the cuboid
     * @param loc2 the second location of the cuboid
     */
    public Cuboid(Location loc1, Location loc2) {
        this.pos1[0] = loc1.getBlockX();
        this.pos1[1] = loc1.getBlockY();
        this.pos1[2] = loc1.getBlockZ();
        this.pos2[0] = loc2.getBlockX();
        this.pos2[1] = loc2.getBlockY();
        this.pos2[2] = loc2.getBlockZ();
        sortPos();
    }

    /**
     * Sorts the positions to ensure pos1 is always less than or equal to pos2.
     */
    private void sortPos() {
        int[] temp = new int[3];
        for (int i = 0; i < 3; i++) {
            if (pos1[i] > pos2[i]) {
                temp[i] = pos1[i];
                pos1[i] = pos2[i];
                pos2[i] = temp[i];
            }
        }
    }

    /**
     * Gets the first position of the cuboid.
     *
     * @return the first position of the cuboid
     */
    public int[] getPos1() {
        return pos1.clone();
    }

    /**
     * Sets the first position of the cuboid.
     *
     * @param pos1 the new first position of the cuboid
     */
    public void setPos1(int[] pos1) {
        this.pos1 = pos1.clone();
    }

    /**
     * Gets the second position of the cuboid.
     *
     * @return the second position of the cuboid
     */
    public int[] getPos2() {
        return pos2.clone();
    }

    /**
     * Sets the second position of the cuboid.
     *
     * @param pos2 the new second position of the cuboid
     */
    public void setPos2(int[] pos2) {
        this.pos2 = pos2.clone();
    }

    /**
     * Gets the first location of the cuboid in the specified world.
     *
     * @param world the world in which the cuboid is located
     * @return the first location of the cuboid
     */
    public Location getLoc1(World world) {
        return new Location(world, pos1[0], pos1[1], pos1[2]);
    }

    /**
     * Gets the second location of the cuboid in the specified world.
     *
     * @param world the world in which the cuboid is located
     * @return the second location of the cuboid
     */
    public Location getLoc2(World world) {
        return new Location(world, pos2[0], pos2[1], pos2[2]);
    }

    /**
     * Gets the x-coordinate of the first position.
     *
     * @return the x-coordinate of the first position
     */
    public int x1() {
        return pos1[0];
    }

    /**
     * Gets the y-coordinate of the first position.
     *
     * @return the y-coordinate of the first position
     */
    public int y1() {
        return pos1[1];
    }

    /**
     * Gets the z-coordinate of the first position.
     *
     * @return the z-coordinate of the first position
     */
    public int z1() {
        return pos1[2];
    }

    /**
     * Gets the x-coordinate of the second position.
     *
     * @return the x-coordinate of the second position
     */
    public int x2() {
        return pos2[0];
    }

    /**
     * Gets the y-coordinate of the second position.
     *
     * @return the y-coordinate of the second position
     */
    public int y2() {
        return pos2[1];
    }

    /**
     * Gets the z-coordinate of the second position.
     *
     * @return the z-coordinate of the second position
     */
    public int z2() {
        return pos2[2];
    }

    /**
     * Gets the length of the cuboid along the x-axis.
     *
     * @return the length of the cuboid along the x-axis
     */
    public int xLength() {
        return pos2[0] - pos1[0];
    }

    /**
     * Gets the length of the cuboid along the y-axis.
     *
     * @return the length of the cuboid along the y-axis
     */
    public int yLength() {
        return pos2[1] - pos1[1];
    }

    /**
     * Gets the length of the cuboid along the z-axis.
     *
     * @return the length of the cuboid along the z-axis
     */
    public int zLength() {
        return pos2[2] - pos1[2];
    }

    /**
     * Gets the square area of the cuboid's base (x and z dimensions).
     *
     * @return the square area of the cuboid's base
     */
    public int getSquare() {
        return xLength() * zLength();
    }

    /**
     * Gets the volume of the cuboid.
     *
     * @return the volume of the cuboid
     */
    public int getVolume() {
        return xLength() * yLength() * zLength();
    }

    /**
     * Checks if this cuboid intersects with another cuboid.
     *
     * @param cuboid the other cuboid to check for intersection
     * @return true if the cuboids intersect, false otherwise
     */
    public boolean intersectWith(Cuboid cuboid) {
        return x1() <= cuboid.x2() && x2() >= cuboid.x1() && y1() <= cuboid.y2() && y2() >= cuboid.y1() && z1() <= cuboid.z2() && z2() >= cuboid.z1();
    }

    /**
     * Checks if this cuboid contains another cuboid.
     *
     * @param cuboid the other cuboid to check for containment
     * @return true if this cuboid contains the other cuboid, false otherwise
     */
    public boolean contain(Cuboid cuboid) {
        return x1() <= cuboid.x1() && x2() >= cuboid.x2() && y1() <= cuboid.y1() && y2() >= cuboid.y2() && z1() <= cuboid.z1() && z2() >= cuboid.z2();
    }

    /**
     * Checks if this cuboid contains the specified coordinates.
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @param z the z-coordinate to check
     * @return true if this cuboid contains the specified coordinates, false otherwise
     */
    public boolean contain(int x, int y, int z) {
        return x1() <= x && x2() >= x && y1() <= y && y2() >= y && z1() <= z && z2() >= z;
    }

    /**
     * Checks if this cuboid is contained by another cuboid.
     *
     * @param cuboid the other cuboid to check for containment
     * @return true if this cuboid is contained by the other cuboid, false otherwise
     */
    public boolean containedBy(Cuboid cuboid) {
        return cuboid.contain(this);
    }

    /**
     * Calculates the difference in square area between this cuboid and another cuboid.
     *
     * @param cuboid the other cuboid to compare with
     * @return the difference in square area
     */
    public int minusSquareWith(Cuboid cuboid) {
        return getSquare() - cuboid.getSquare();
    }

    /**
     * Calculates the difference in volume between this cuboid and another cuboid.
     *
     * @param cuboid the other cuboid to compare with
     * @return the difference in volume
     */
    public int minusVolumeWith(Cuboid cuboid) {
        return getVolume() - cuboid.getVolume();
    }

    /**
     * Expands the cuboid upwards by the specified size.
     *
     * @param size the amount to expand the cuboid upwards
     */
    public void addUp(int size) {
        pos2[1] += size;
    }

    /**
     * Expands the cuboid downwards by the specified size.
     *
     * @param size the amount to expand the cuboid downwards
     */
    public void addDown(int size) {
        pos1[1] -= size;
    }

    /**
     * Expands the cuboid northwards by the specified size.
     *
     * @param size the amount to expand the cuboid northwards
     */
    public void addNorth(int size) {
        pos1[2] -= size;
    }

    /**
     * Expands the cuboid southwards by the specified size.
     *
     * @param size the amount to expand the cuboid southwards
     */
    public void addSouth(int size) {
        pos2[2] += size;
    }

    /**
     * Expands the cuboid eastwards by the specified size.
     *
     * @param size the amount to expand the cuboid eastwards
     */
    public void addEast(int size) {
        pos2[0] += size;
    }

    /**
     * Expands the cuboid westwards by the specified size.
     *
     * @param size the amount to expand the cuboid westwards
     */
    public void addWest(int size) {
        pos1[0] -= size;
    }
}