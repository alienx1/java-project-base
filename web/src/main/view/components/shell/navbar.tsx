import {
  Navbar as HeroUINavbar,
  NavbarContent,
  NavbarBrand,
} from "@heroui/navbar";
import { Button } from "@heroui/button";
import NextLink from "next/link";

import {
  Logo,
} from "@/components/shell/icons";
import { Bars3Icon } from "@heroicons/react/24/outline";
import { ThemeSwitch } from "./theme-switch";

export const Navbar = ({ onToggleSidebar }: { onToggleSidebar: () => void }) => {

  return (
    <HeroUINavbar maxWidth="full" position="sticky" className="bg-background border-b border-border shadow-sm" >
      <NavbarContent className="basis-1/5 sm:basis-full" justify="start">
        <Button
          isIconOnly
          size="sm"
          variant="light"
          onClick={onToggleSidebar}
          className="mr-2"
        >
          <Bars3Icon className="h-5 w-5 text-default-500" />
        </Button>

        <NavbarBrand as="li" className="gap-3 max-w-fit">
          <NextLink className="flex justify-start items-center gap-1" href="/">
            <Logo />
            <p className="font-bold text-inherit">ACME</p>
          </NextLink>
        </NavbarBrand>

      </NavbarContent>
      <NavbarContent className="basis-1/5 sm:basis-full" justify="end">
        <ThemeSwitch />
      </NavbarContent>
    </HeroUINavbar>
  );
};

