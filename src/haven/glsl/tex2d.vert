#pp header

#pp entry tex2d_v
#pp order 500
void tex2d_v();

#pp main

void tex2d_v()
{
    gl_TexCoord[0] = gl_MultiTexCoord0;
}
