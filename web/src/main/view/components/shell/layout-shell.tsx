'use client';

import { useState } from "react";
import { Navbar } from "@/components/shell/navbar";
import Sidebar from "@/components/shell/sidenav";
import { Link } from "@heroui/link";

export default function LayoutShell({ children }: { children: React.ReactNode }) {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    return (
        <div className="flex h-screen overflow-hidden">
            <Sidebar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen} />
            <div className="flex flex-col flex-1 overflow-hidden">
                <Navbar onToggleSidebar={() => setIsSidebarOpen((prev) => !prev)} />
                <Sidebar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen} />
                <main className="flex-1 overflow-y-auto p-8">
                    <section className="flex flex-col gap-4 py-8 md:py-10">
                        {children}
                    </section>
                </main>
                <footer className="w-full flex items-center justify-center py-3">
                    <Link
                        isExternal
                        className="flex items-center gap-1 text-current"
                        href="https://heroui.com?utm_source=next-app-template"
                        title="heroui.com homepage"
                    >
                        <span className="text-default-600">Powered by</span>
                        <p className="text-primary">HeroUI</p>
                    </Link>
                </footer>
            </div>
        </div>
    );
}
