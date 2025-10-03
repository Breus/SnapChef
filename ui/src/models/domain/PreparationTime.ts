export const PreparationTime = {
    MIN_0_15: "0 - 15 mins",
    MIN_15_30: "15 - 30 mins",
    MIN_30_45: "30 - 45 mins",
    MIN_45_60: "45 - 60 mins",
    HOUR_PLUS: "1+ hours",
} as const;

export type PreparationTime = typeof PreparationTime[keyof typeof PreparationTime];