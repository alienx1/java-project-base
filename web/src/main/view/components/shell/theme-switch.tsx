"use client";

import { FC } from "react";
import { VisuallyHidden } from "@react-aria/visually-hidden";
import { SwitchProps, useSwitch } from "@heroui/switch";
import { useTheme } from "next-themes";
import { useIsSSR } from "@react-aria/ssr";
import clsx from "clsx";
import { SunIcon, MoonIcon } from "@heroicons/react/24/outline";

export interface ThemeSwitchProps {
  className?: string;
  classNames?: SwitchProps["classNames"];
}

export const ThemeSwitch: FC<ThemeSwitchProps> = ({
  className,
  classNames,
}) => {
  const { theme, setTheme } = useTheme();
  const isSSR = useIsSSR();

  // Function to toggle between light and dark themes
  const onChange = () => {
    theme === "light" ? setTheme("dark") : setTheme("light");
  };

  const {
    Component,
    slots,
    isSelected,
    getBaseProps,
    getInputProps,
    getWrapperProps,
  } = useSwitch({
    isSelected: theme === "light" || isSSR,
    "aria-label": `Switch to ${theme === "light" || isSSR ? "dark" : "light"} mode`,
    onChange,
  });

  return (
    <Component
      {...getBaseProps({
        className: clsx(
          "px-px transition-opacity hover:opacity-80 cursor-pointer",
          className,
          classNames?.base,
        ),
      })}
    >
      <VisuallyHidden>
        <input {...getInputProps()} />
      </VisuallyHidden>
      <div
        {...getWrapperProps()}
        className={clsx(
          "w-auto h-auto bg-transparent rounded-lg flex items-center justify-center",
          "group-data-[selected=true]:bg-transparent !text-default-500 pt-px px-0 mx-0", 
          classNames?.wrapper
        )}
      >
        {/* Conditionally render Sun or Moon icon based on theme */}
        {isSelected || isSSR ? (
          <SunIcon className="w-8 h-8 text-yellow-500" />
        ) : (
          <MoonIcon className="w-8 h-8 text-blue-500" />
        )}
      </div>
    </Component>
  );
};
