type AuthLayoutProps = {
  children: React.ReactNode
}

export function AuthLayout({ children }: AuthLayoutProps) {
  return (
    <div className='container grid h-svh max-w-none items-center justify-center'>
      <div className='mx-auto flex w-full flex-col items-center justify-center sm:w-[400px] mt-20'>
        <div className='w-full'>
          {children}
        </div>
      </div>
    </div>
  )
}