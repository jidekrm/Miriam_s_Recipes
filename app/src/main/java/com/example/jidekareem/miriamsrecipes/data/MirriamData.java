package com.example.jidekareem.miriamsrecipes.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MirriamData implements Parcelable {

    private final int id;
    public final String name;
    private final int servings;
    private final List<Ingredients> ingredients;
    public final List<Steps> steps;
    private final String image;

    public MirriamData(int id, String name, int servings, List<Ingredients> ingredients, List<Steps> steps, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.ingredients = ingredients;
        this.steps = steps;
        this.image = image;
    }

    protected MirriamData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        steps = in.createTypedArrayList(Steps.CREATOR);
        image = in.readString();
    }

    public static final Creator<MirriamData> CREATOR = new Creator<MirriamData>() {
        @Override
        public MirriamData createFromParcel(Parcel in) {
            return new MirriamData(in);
        }

        @Override
        public MirriamData[] newArray(int size) {
            return new MirriamData[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeString(image);
    }

}
